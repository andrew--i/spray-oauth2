package ai.akka.actor

import ai.akka.actor.ClientDetailsServiceActor.{ClientDetails, LoadClientByClientIdMessage}

/**
 * Created by Андрей Смирнов on 20.08.2014.
 */
class ClientDetailsServiceActor extends OAuth2ServiceActor {
  override def receive: Receive = {
    case LoadClientByClientIdMessage(clientId) => sender ! ClientDetails()
  }
}

object ClientDetailsServiceActor {

  case class LoadClientByClientIdMessage(clientId: String)

  case class ClientDetails()

}
