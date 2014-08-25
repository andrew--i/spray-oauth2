package ai.akka.actor

import ai.akka.actor.ApprovalServiceActor.ApproveRequestMessage
import ai.akka.actor.AuthenticationServiceActor.AuthenticateUserMessage
import ai.akka.actor.OAuth2RequestFactoryActor._
import ai.akka.actor.OAuth2ValidateActor._
import ai.akka.exception.Exception.OAuthServiceException
import ai.akka.oauth2.Constants
import ai.akka.oauth2.model.AuthorizationRequest
import ai.akka.service.approval.InMemoryApprovalService
import ai.akka.service.authentication.Model.Authentication
import ai.akka.service.authentication.SimpleAuthenticationService
import ai.akka.service.client.InMemoryClientDetailsService
import ai.akka.service.validate.OAuth2ValidateRequestService
import akka.actor.SupervisorStrategy.Restart
import akka.actor._
import akka.http.model.ContentTypes._
import akka.http.model._
import akka.http.model.headers._
import akka.pattern.ask
import akka.stream.scaladsl.Flow
import akka.stream.{FlowMaterializer, MaterializerSettings}
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.collection.immutable
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * Actor for processing incoming HttpRequests
 */
class RequestProcessingActor() extends OAuth2ServiceActor {

  /**
   * Actor with in-memory storage for client details
   */
  class InMemoryClientDetailsServiceActor extends ClientDetailsServiceActor with InMemoryClientDetailsService

  /**
   * Actor with default validation implementation
   */
  class OAuth2ValidateRequestServiceActor extends OAuth2ValidateActor with OAuth2ValidateRequestService

  /**
   * Actor with in-memory storage for approvals
   */
  class InMemoryApprovalServiceActor extends ApprovalServiceActor with InMemoryApprovalService

  /**
   * Actor with default authentication implementation
   */
  class SimpleAuthenticationServiceActor extends AuthenticationServiceActor with SimpleAuthenticationService

  /**
   * Reference to client details service
   */
  val clientDetailsService: ActorRef = context.actorOf(Props(new InMemoryClientDetailsServiceActor()))
  /**
   * Reference to validation service
   */
  val oauth2ValidateActor: ActorRef = context.actorOf(Props(new OAuth2ValidateRequestServiceActor()))
  /**
   * Reference to factory of authorization requests
   */
  val oauth2RequestFactory: ActorRef = context.actorOf(Props[OAuth2RequestFactoryActor])
  /**
   * Reference to approval service
   */
  val approvalService: ActorRef = context.actorOf(Props(new InMemoryApprovalServiceActor()))
  /**
   * Reference to authentication service
   */
  val authenticationService: ActorRef = context.actorOf(Props(new SimpleAuthenticationServiceActor()))

  override def receive: Receive = {
    case request: HttpRequest =>
      val source: ActorRef = sender()
      val response: Future[HttpResponse] = createResponse(request, source)
      response onComplete {
        case Success(r: HttpResponse) => source ! r
        case Failure(e: Throwable) => source ! createInternalServerErrorResponse(e)
      }
    case _ =>
      val source: ActorRef = sender()
      source ! createNotFountResponse()
  }

  /**
   * Creates response with header 'content-type application/json
   * @param status status of response
   * @param content body of response
   * @return response
   */
  def createResponseWithJSONContent(status: StatusCode, content: String): HttpResponse = {
    val headers: immutable.Seq[`Content-Type`] = scala.collection.immutable.Seq(`Content-Type`(`application/json`))
    val jsonFormattedString: String = content.toJson.prettyPrint
    HttpResponse(status, entity = HttpEntity(`application/json`, jsonFormattedString), headers = headers)
  }

  /**
   * Creates response with 500 status code by exception
   * @param e exception information
   * @return response
   */
  def createInternalServerErrorResponse(e: Throwable): HttpResponse = {
    createResponseWithJSONContent(StatusCodes.InternalServerError, e.getMessage)
  }

  /**
   * Create response with 404 status code
   * @return response
   */
  def createNotFountResponse(): HttpResponse = {
    createResponseWithJSONContent(StatusCodes.NotFound, "Unknown resource!")
  }

  /**
   * Create response future by request
   * @param request request
   * @param httpResponseActorRef reference to actor which waiting http response
   * @return response future
   */
  def createResponse(request: HttpRequest, httpResponseActorRef: ActorRef): Future[HttpResponse] = {
    request match {
      case HttpRequest(HttpMethods.GET, Uri.Path("/oauth/authorize"), _, _, _) =>
        Flow(createAuthorizationRequestAndUserAuthentication(request, httpResponseActorRef))
          .mapFuture(context => validateContext(context))
          .mapFuture(context => approveRequest(context))
          .map(context => createResponse(context))
          .toFuture(FlowMaterializer(MaterializerSettings()))
          .mapTo[HttpResponse]
      case _ => Future(createNotFountResponse())
    }
  }

  /**
   * Supervision strategy for all child actor (services)
   */
  override val supervisorStrategy =
    OneForOneStrategy() {
      case t: OAuthServiceException => t.httpResponseActor ! createInternalServerErrorResponse(t)
        Restart
      case e: Throwable => Restart
    }

  /**
   * Create Request Processing Context by request and reference to actor with waiting http response
   * @param request http request
   * @param httpResponseActorRef reference to actor
   * @return request processing context future
   */
  def createAuthorizationRequestAndUserAuthentication(request: HttpRequest, httpResponseActorRef: ActorRef): Future[RequestProcessingContext] = {
    for {r <- (oauth2RequestFactory ? CreateAuthorizationRequestMessage(request, clientDetailsService, httpResponseActorRef)).mapTo[AuthorizationRequest]
         a <- (authenticationService ? AuthenticateUserMessage(request)).mapTo[Authentication]}
    yield RequestProcessingContext(r, a, httpResponseActorRef)
  }

  /**
   * Validate request processing context
   * @param context request processing context
   * @return request processing context
   */
  def validateContext(context: RequestProcessingContext): Future[RequestProcessingContext] = {
    val message: ValidateAuthorizationRequestMessage = ValidateAuthorizationRequestMessage(context.authorizationRequest, context.userAuthentication, context.httpResponseActorRef)
    for (req <- (oauth2ValidateActor ? message).mapTo[AuthorizationRequest])
    yield RequestProcessingContext(req, context.userAuthentication, context.httpResponseActorRef)
  }

  /**
   * Approve authorization request of request processing context
   * @param context request processing context
   * @return request processing context
   */
  def approveRequest(context: RequestProcessingContext): Future[RequestProcessingContext] = {
    for (req <- (approvalService ? ApproveRequestMessage(context.authorizationRequest)).mapTo[AuthorizationRequest])
    yield RequestProcessingContext(req, context.userAuthentication, context.httpResponseActorRef)
  }

  /**
   * Create http response by request processing context
   * @param context request processing context
   * @return http response
   */
  def createResponse(context: RequestProcessingContext): HttpResponse = {
    val request: AuthorizationRequest = context.authorizationRequest
    if (request.approved) {
      if (request.responseTypes.contains(Constants.TOKEN_RESPONSE_TYPE))
        return createImplicitGrandResponse(context)
      if (request.responseTypes.contains(Constants.CODE_RESPONSE_TYPE))
        return createAuthorizationCodeResponse(context)
    }

    createUserApprovalPageResponse(context)
  }

  /**
   * Create response which redirects to user approval page
   * @param context request processing context
   * @return http response
   */
  def createUserApprovalPageResponse(context: RequestProcessingContext): HttpResponse = {
    createResponseWithJSONContent(StatusCodes.OK, "response from createUserApprovalPageResponse")
  }

  /**
   * Create response for implicit grand type
   * @param context request processing context
   * @return http response
   */
  def createImplicitGrandResponse(context: RequestProcessingContext): HttpResponse = {
    createResponseWithJSONContent(StatusCodes.OK, "response from createImplicitGrandResponse")
  }

  /**
   * Create response for authorization code grand type
   * @param context request processing context
   * @return http response
   */
  def createAuthorizationCodeResponse(context: RequestProcessingContext): HttpResponse = {
    createResponseWithJSONContent(StatusCodes.OK, "response from createAuthorizationCodeResponse")
  }
}

/**
 * Class with information about request processing
 * @param authorizationRequest authorization request
 * @param userAuthentication authentication information
 * @param httpResponseActorRef reference to actor which waiting http response
 */
case class RequestProcessingContext(authorizationRequest: AuthorizationRequest, userAuthentication: Authentication, httpResponseActorRef: ActorRef);


