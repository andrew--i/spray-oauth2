package ai.spray.server

import ai.spray.service.OAuth2Service
import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.util.Timeout
import spray.can.Http

import scala.concurrent.duration._

/**
 * Created by Andrew on 06.08.2014.
 * Запуск провайдера
 */
object Main extends OAuth2SslConfiguration {
  implicit val timeout: Timeout = 3 seconds

  def main(args: Array[String]) {
    implicit val system = ActorSystem("sprayOAuth2System")

    IO(Http) ! Http.Bind(system.actorOf(Props[OAuth2Service]), interface = "localhost", port = 8080)
  }
}
