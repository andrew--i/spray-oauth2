package ai.akka.actor

import ai.akka.actor.OAuth2RequestFactoryActor._
import ai.akka.actor.OAuth2ValidateActor._
import ai.akka.exception.Exception.OAuthServiceException
import ai.akka.oauth2.model.AuthorizationRequest
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
 * Created by Андрей Смирнов on 20.08.2014.
 */


class RequestProcessingActor() extends OAuth2ServiceActor {

  class InMemoryClientDetailsServiceActor extends ClientDetailsServiceActor with InMemoryClientDetailsService
  class OAuth2ValidateRequestServiceActor extends OAuth2ValidateActor with OAuth2ValidateRequestService

  val clientDetailsService: ActorRef = context.actorOf(Props(new InMemoryClientDetailsServiceActor()))
  val oauth2ValidateActor: ActorRef = context.actorOf(Props(new OAuth2ValidateRequestServiceActor()))
  val oauth2RequestFactory: ActorRef = context.actorOf(Props[OAuth2RequestFactoryActor])

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

  def createResponseWithJSONContent(status: StatusCode, content: String): HttpResponse = {
    val headers: immutable.Seq[`Content-Type`] = scala.collection.immutable.Seq(`Content-Type`(`application/json`))
    val jsonFormattedString: String = content.toJson.prettyPrint
    HttpResponse(status, entity = HttpEntity(`application/json`, jsonFormattedString), headers = headers)
  }

  def createInternalServerErrorResponse(e: Throwable): HttpResponse = {
    createResponseWithJSONContent(StatusCodes.InternalServerError, e.getMessage)
  }

  def createNotFountResponse(): HttpResponse = {
    createResponseWithJSONContent(StatusCodes.NotFound, "Unknown resource!")
  }

  def createResponse(request: HttpRequest, httpResponseActorRef: ActorRef): Future[HttpResponse] = {
    request match {
      case HttpRequest(HttpMethods.GET, Uri.Path("/oauth/authorize"), _, _, _) =>
        Flow((oauth2RequestFactory ? CreateAuthorizationRequestMessage(request, clientDetailsService, httpResponseActorRef)).mapTo[AuthorizationRequest])
          .mapFuture(r => (oauth2ValidateActor ? ValidateAuthorizationRequestMessage(r, httpResponseActorRef)).mapTo[AuthorizationRequest])
          .map(r => createResponseWithJSONContent(StatusCodes.OK, r.toString))
          .toFuture(FlowMaterializer(MaterializerSettings()))
          .mapTo[HttpResponse]
      case _ => Future(createNotFountResponse())
    }
  }

  override val supervisorStrategy =
    OneForOneStrategy() {
      case t: OAuthServiceException => t.httpResponseActor ! createInternalServerErrorResponse(t)
        Restart
      case e: Throwable => Restart
    }
}
