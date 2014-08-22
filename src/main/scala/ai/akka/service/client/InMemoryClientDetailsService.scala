package ai.akka.service.client

import ai.akka.actor.ClientDetailsService
import ai.akka.actor.ClientDetailsServiceActor.ClientDetails

/**
 * Created by Андрей Смирнов on 22.08.2014.
 */
trait InMemoryClientDetailsService extends ClientDetailsService {
  var clients: Set[ClientDetails] = Set.empty

  override def loadClientDetailsByClientId(clientId: String): ClientDetails =
    clients.find(d => d.clientId == clientId) match {
      case Some(c) => c
      case None => throw new IllegalArgumentException("Client with specified id did not find")
    }
}
