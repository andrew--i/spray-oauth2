package ai.akka.server

import ai.akka.actor.RequestProcessingActor
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.Http
import akka.http.model.HttpResponse
import akka.io.IO
import akka.pattern._
import akka.stream.scaladsl.Flow
import akka.stream.{FlowMaterializer, MaterializerSettings}
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._


/**
 * Main object. it starts oauth2 provider
 */
object Main {

  def main(args: Array[String]) {
    implicit val system = ActorSystem("sprayOAuth2System")
    implicit val ec = system.dispatcher
    implicit val timeout = Timeout(5.seconds)

    val settings = MaterializerSettings()
    val materializer = FlowMaterializer(settings)

    val requestProcessingActor: ActorRef = system.actorOf(Props[RequestProcessingActor])

    val future: Future[Any] = IO(Http) ? Http.Bind(interface = "localhost", port = 8080)
    future.onSuccess({
      case serverBinding: Http.ServerBinding =>
        Flow(serverBinding.connectionStream).foreach(conn =>
          Flow(conn.requestPublisher)
            .mapFuture(request => (requestProcessingActor ? request).mapTo[HttpResponse])
            .produceTo(materializer, conn.responseSubscriber)
        ).consume(materializer)
    })
    future.onFailure {
      case e: Throwable => system.shutdown()
    }
  }
}


