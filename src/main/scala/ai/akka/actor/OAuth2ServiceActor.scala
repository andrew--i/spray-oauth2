package ai.akka.actor

import akka.actor.Actor
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
 * Created by Андрей Смирнов on 20.08.2014.
 */
trait OAuth2ServiceActor extends Actor {
  implicit val ec: ExecutionContext = context.dispatcher
  implicit val duration: Timeout = Timeout(100.millis)

}
