package ai.spray.service

import ai.spray.oauth2.Exception.RequestProcessException
import ai.spray.oauth2.actor.message.RequestMessage.{BaseRequest, AuthorizationGetRequestMessage, AuthorizationPostRequestMessage}
import ai.spray.service.authorize.AuthorizeService
import akka.actor.{Actor, OneForOneStrategy}
import akka.actor.SupervisorStrategy.Restart
import org.json4s.{DefaultFormats, Formats}
import spray.http.StatusCode
import spray.http.StatusCodes._
import spray.httpx.Json4sSupport
import spray.routing.{RequestContext, HttpServiceActor, Route}


/**
 * Created by Andrew on 06.08.2014.
 * Обработчик запросов
 */
class OAuth2ServiceActor extends HttpServiceActor with AuthorizeService {

  override def receive: Receive = runRoute(oauth2Route)


  def oauth2Route: Route =
    path("oauth" / "authorize") {
      get {
        ctx => authorizeClient(AuthorizationGetRequestMessage(ctx))
      } ~
        post {
          ctx => authorizeClient(AuthorizationPostRequestMessage(ctx))
        }
    }

  override val supervisorStrategy =
    OneForOneStrategy() {
      case e: RequestProcessException =>
        e.ctx.complete(InternalServerError, e.getMessage)
        Restart
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
