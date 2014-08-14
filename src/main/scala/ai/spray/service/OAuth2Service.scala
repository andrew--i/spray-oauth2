package ai.spray.service

import ai.spray.oauth2.actor.message.RequestMessage
import RequestMessage.{AuthorizationGetRequestMessage, AuthorizationPostRequestMessage}
import ai.spray.service.authorize.AuthorizeService
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
        authorizeClient(AuthorizationGetRequestMessage())
      } ~
        post {
          authorizeClient(AuthorizationPostRequestMessage())
        }
    }
}
