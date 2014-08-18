package ai.spray.service.authorize

import ai.spray.oauth2.Exception.RequestProcessException
import ai.spray.oauth2.actor.message.RequestMessage.{AuthorizationGetRequestMessage, AuthorizationPostRequestMessage, BaseRequest}
import akka.actor.{Actor, ActorRef, Props}
import org.json4s.{DefaultFormats, Formats}
import spray.http.{StatusCodes, StatusCode}
import spray.httpx.Json4sSupport
import spray.routing._

/**
 * Created by Andrew on 10.08.2014.
 * Обработчик endpoint-авторизации
 */
class AuthorizeServiceActor() extends OAuth2Service {

  override def receive: Receive = {
    case getRequest: AuthorizationGetRequestMessage =>
      completeRequest(getRequest, StatusCodes.OK, "It`s works");
    case postRequest: AuthorizationPostRequestMessage =>
      throw_(new NotImplementedError("AuthorizationPostRequest не реализована обработка"), postRequest.ctx)
    case _ => throw new IllegalArgumentException("")
  }

}

trait OAuth2Service extends Actor with Json4sSupport {

  override implicit def json4sFormats: Formats = DefaultFormats

  def throw_(e: Throwable, ctx: RequestContext): Unit = {
    throw new RequestProcessException(e, ctx)
  }

  def completeRequest[T <: AnyRef](request: BaseRequest, status: StatusCode, body: T): Unit = {
    request.ctx.complete(status, body)
  }
}

trait AuthorizeService extends Actor {
  val authorizeActor: ActorRef = context.actorOf(Props[AuthorizeServiceActor])

  def authorizeClient(request: BaseRequest): Unit = {
    authorizeActor ! request
  }
}
