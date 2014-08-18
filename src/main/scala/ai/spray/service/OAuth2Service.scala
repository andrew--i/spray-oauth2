package ai.spray.service

import ai.spray.oauth2.Exception.RequestProcessException
import ai.spray.oauth2.actor.message.RequestMessage.{AuthorizationGetRequestMessage, AuthorizationPostRequestMessage}
import ai.spray.service.authorize.AuthorizeService
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy.Restart
import spray.http.StatusCodes._
import spray.routing.{HttpServiceActor, Route}


/**
 * Created by Andrew on 06.08.2014.
 * Обработчик запросов
 */
class OAuth2Service extends HttpServiceActor with AuthorizeService {

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
