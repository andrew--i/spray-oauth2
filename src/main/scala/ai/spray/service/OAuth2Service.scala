package ai.spray.service

import akka.actor.Props
import spray.http.HttpResponse
import spray.routing.{HttpServiceActor, Route}



/**
 * Created by Andrew on 06.08.2014.
 * Обработчик запросов
 */
class OAuth2Service extends HttpServiceActor with PerRequestCreator{

  override def receive: Receive = runRoute(oauth2Route)


  def oauth2Route: Route =
    path("oauth" / "authorize") {
      get {
        parameterMap {
          params =>
            processRequestParams(params)
        }
      }
    } ~
      path("") {
        get {
          complete(HttpResponse(entity = "hello world"))
        }
      }

  def redirectToErrorPage(error: Throwable): Route = {
    complete(HttpResponse(entity = s"error $error"))
  }

  def processRequestParams(message:Any):Route = {
    ctx => perRequest(ctx, Props(new AuthorizeServiceActor()), message);
  }

}
