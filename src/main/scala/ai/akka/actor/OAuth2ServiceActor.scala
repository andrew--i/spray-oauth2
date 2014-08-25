package ai.akka.actor

import akka.actor.Actor
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
 * Trait which encapsulates execution context and time out for futures
 */
trait OAuth2ServiceActor extends Actor {
  implicit val ec: ExecutionContext = context.dispatcher
  implicit val duration: Timeout = Timeout(100.millis)

}
