package ai.spray.server

import ai.spray.service.OAuth2Service
import akka.actor.{Props, ActorSystem}
import akka.io.IO
import spray.can.Http

/**
 * Created by Andrew on 06.08.2014.
 */
object Main {
  def main(args: Array[String]) {
    implicit val system = ActorSystem("sprayOAuth2System")
    IO(Http) ! Http.Bind(system.actorOf(Props[OAuth2Service]), interface = "localhost", port = 8080)
  }
}
