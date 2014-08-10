package ai.spray.service

import akka.actor.Props
import akka.pattern.ask
import spray.http.HttpResponse
import spray.routing.{HttpServiceActor, Route}

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Try



/**
 * Created by Andrew on 06.08.2014.
 * Обработчик запросов
 */
class OAuth2Service extends HttpServiceActor {

  val authorizeActor = context.actorOf(Props[AuthorizeServiceActor])

  import context.dispatcher

  override def receive: Receive = runRoute(oauth2Route)


  def oauth2Route: Route =
    path("oauth" / "authorize") {
      get {
        parameterMap {
          params =>
            val future: Future[HttpResponse] = (authorizeActor ask params)(5 second).mapTo[HttpResponse]
            val l = 90
            val value: Option[Try[HttpResponse]] = future.value
            complete {
              Future {
                "hello world from future"
              }
            }
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

}
