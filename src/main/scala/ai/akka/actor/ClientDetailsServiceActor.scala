package ai.akka.actor


import ai.akka.actor.ClientDetailsServiceActor.LoadClientByClientIdMessage
import ai.akka.service.client.ClientDetailsService


/**
 * Created by Андрей Смирнов on 20.08.2014.
 */
trait ClientDetailsServiceActor extends OAuth2ServiceActor with ClientDetailsService {
  override def receive: Receive = {
    case LoadClientByClientIdMessage(clientId) => sender ! findClientDetailsByClientId(clientId)
  }
}

object ClientDetailsServiceActor {
  case class LoadClientByClientIdMessage(clientId: String)
}
