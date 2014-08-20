package ai.akka.actor

import ai.akka.actor.ClientDetailsServiceActor.{ClientDetails, LoadClientByClientId}
import akka.actor.Actor

/**
 * Created by Андрей Смирнов on 20.08.2014.
 */
class ClientDetailsServiceActor extends Actor {
  override def receive: Receive = {
    case LoadClientByClientId(clientId) => ClientDetails()
    case _ =>
  }


}

object ClientDetailsServiceActor {

  case class LoadClientByClientId(clientId: String)

  case class ClientDetails()

}
