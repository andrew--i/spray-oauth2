package ai.spray.service

import spray.http.HttpResponse
import spray.routing.{Route, HttpServiceActor}

/**
 * Created by Andrew on 06.08.2014.
 */
class OAuth2Service extends HttpServiceActor {

  override def receive: Receive = runRoute(oauth2Route)

  def oauth2Route: Route = path("oauth" / "authorize" ) {
    get {
      complete(HttpResponse(entity = "oauth/authorize"))
    }
  } ~ path("") {
    get {
      complete(HttpResponse(entity = "hello world"))
    }
  }
}
