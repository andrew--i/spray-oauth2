package ai.spray.server

import akka.actor.ActorSystem
import akka.http.Http
import akka.http.model.{StatusCodes, HttpResponse}
import akka.io.IO
import akka.pattern._
import akka.stream.{MaterializerSettings, FlowMaterializer}
import akka.stream.scaladsl.Flow
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.Future


/**
 * Created by Andrew on 06.08.2014.
 * Запуск провайдера
 */
object Main {


  def main(args: Array[String]) {
    implicit val system = ActorSystem("sprayOAuth2System")
    implicit val ec = system.dispatcher
    implicit val timeout = Timeout(5.seconds)
    val settings = MaterializerSettings()
    val materializer = FlowMaterializer(settings)

    val future: Future[Any] = IO(Http) ? Http.Bind(interface = "localhost", port = 8080)
    future.onSuccess({
      case serverBinding: Http.ServerBinding =>
        Flow(serverBinding.connectionStream).foreach(conn =>
          Flow(conn.requestPublisher)
            .map(request => HttpResponse(status = StatusCodes.OK, entity = request.toString))
            .produceTo(materializer, conn.responseSubscriber)
        ).consume(materializer)
    })
  }
}
