package ai.akka.service.client

import ai.akka.service.client.Model.ClientDetails

/**
 * Trait with in-memory storage for client details
 */
trait InMemoryClientDetailsService extends ClientDetailsService {
  /**
   * Client details
   */
  var clients: Set[ClientDetails] = Set.empty

  /**
   * @see(ai.akka.service.client.ClientDetailsService)
   * @param clientId client identity
   * @return client details
   */
  override def findClientDetailsByClientId(clientId: String): ClientDetails =
    clients.find(d => d.clientId == clientId) match {
      case Some(c) => c
      case None => throw new IllegalArgumentException("Client with specified id did not find")
    }

  /**
   * @see(ai.akka.service.client.ClientDetailsService)
   * @param clientDetails client details
   * @return client details
   */
  override def addClient(clientDetails: ClientDetails): ClientDetails = {
    if (clients.exists(c => c.clientId == clientDetails.clientId))
      throw new IllegalArgumentException("Client with specified id already exists")
    clients = clients + clientDetails
    clientDetails
  }

  /**
   * @see(ai.akka.service.client.ClientDetailsService)
   * @param clientId client identity
   * @return client details
   */
  override def removeClient(clientId: String): ClientDetails = {
    val client: ClientDetails = findClientDetailsByClientId(clientId)
    clients = clients - client
    client
  }
}
