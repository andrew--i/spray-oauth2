package ai.akka.actor


import ai.akka.actor.ClientDetailsServiceActor.LoadClientByClientIdMessage
import ai.akka.service.client.ClientDetailsService


/**
 * Actor for dealing with client details
 */
trait ClientDetailsServiceActor extends OAuth2ServiceActor with ClientDetailsService {
  override def receive: Receive = {
    case LoadClientByClientIdMessage(clientId) => sender ! findClientDetailsByClientId(clientId)
  }
}

object ClientDetailsServiceActor {

  /**
   * Message for ClientDetailsServiceActor which load client details by clientId
   * @param clientId identity of client
   */
  case class LoadClientByClientIdMessage(clientId: String)
}
