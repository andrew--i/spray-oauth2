package ai.akka.actor

import ai.spray.oauth2.model.AuthorizationRequest
import akka.actor.SupervisorStrategy.Restart
import akka.actor._
import akka.http.model._
import akka.pattern.ask
import akka.stream.scaladsl.Flow
import akka.stream.{FlowMaterializer, MaterializerSettings}

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * Created by Андрей Смирнов on 20.08.2014.
 */
class RequestProcessingActor extends OAuth2ServiceActor {

  val clientDetailsService: ActorRef = context.actorOf(Props[ClientDetailsServiceActor])
  val oauth2RequestFactory: ActorRef = context.actorOf(Props[OAuth2RequestFactoryActor])

  override def receive: Receive = {
    case request: HttpRequest =>
      val source: ActorRef = sender()
      val response: Future[HttpResponse] = createResponse(request)
      response onComplete {
        case Success(r: HttpResponse) => source ! r
        case Failure(e: Throwable) => source ! createInternalServerErrorResponse(e)
      }
    case _ => createNotFountResponse()
  }

  def createInternalServerErrorResponse(e: Throwable): HttpResponse = {
    HttpResponse(StatusCodes.InternalServerError, entity = e.getMessage)
  }

  def createNotFountResponse(): HttpResponse = {
    HttpResponse(StatusCodes.NotFound, entity = "Unknown resource!")
  }

  def createResponse(request: HttpRequest): Future[HttpResponse] = {
    request match {
      case HttpRequest(HttpMethods.GET, Uri.Path("/oauth/authorize"), _, _, _) =>
        val future: Future[AuthorizationRequest] = (oauth2RequestFactory ? OAuth2RequestFactoryActor.CreateAuthorizationRequestMessage(request, clientDetailsService)).mapTo[AuthorizationRequest]
        Flow(future)
          .map(r => HttpResponse(StatusCodes.OK, entity = r.toString))
          .toFuture(FlowMaterializer(MaterializerSettings()))
          .mapTo[HttpResponse]
      case _ => Future(createNotFountResponse())
    }
  }

  override val supervisorStrategy =
    OneForOneStrategy() {
      case e: Throwable => Restart
    }
}
