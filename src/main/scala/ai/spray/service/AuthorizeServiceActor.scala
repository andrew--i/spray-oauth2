package ai.spray.service

import akka.actor.Actor

/**
 * Created by Andrew on 10.08.2014.
 */
class AuthorizeServiceActor extends Actor {
  override def receive: Receive = {
    case _ => context.parent ! "response from AuthorizeServiceActor"
  }
}
