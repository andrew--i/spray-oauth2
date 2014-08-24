package ai.akka.service.client

import ai.akka.service.client.Model.ClientDetails

/**
 * Created by Андрей Смирнов on 22.08.2014.
 */
trait InMemoryClientDetailsService extends ClientDetailsService {
  var clients: Set[ClientDetails] = Set.empty

  override def findClientDetailsByClientId(clientId: String): ClientDetails =
    clients.find(d => d.clientId == clientId) match {
      case Some(c) => c
      case None => throw new IllegalArgumentException("Client with specified id did not find")
    }

  override def addClient(clientDetails: ClientDetails): ClientDetails = {
    if (clients.exists(c => c.clientId == clientDetails.clientId))
      throw new IllegalArgumentException("Client with specified id already exists")
    clients = clients + clientDetails
    clientDetails
  }

  override def removeClient(clientId: String): ClientDetails = {
    val client: ClientDetails = findClientDetailsByClientId(clientId)
    clients = clients - client
    client
  }
}
