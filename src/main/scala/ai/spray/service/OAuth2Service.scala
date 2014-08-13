package ai.spray.service

import ai.spray.oauth2.Request.{AuthorizationGetRequest, AuthorizationPostRequest}
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
        authorizeClient(AuthorizationGetRequest())
      } ~
        post {
          authorizeClient(AuthorizationPostRequest())
        }
    }
}
