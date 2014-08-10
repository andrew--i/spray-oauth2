package ai.spray.service

import akka.actor.Actor
import spray.http.HttpResponse

/**
 * Created by Andrew on 10.08.2014.
 */
class AuthorizeServiceActor extends Actor {
  override def receive: Receive = {
    case _ => HttpResponse(entity = "response from AuthorizeServiceActor")
  }
}
