package ai.akka.service.client

import ai.akka.service.client.Model.ClientDetails

/**
 * Trait of client details service
 */
trait ClientDetailsService {
  /**
   * The method finds client details by client identity
   * @param clientId client identity
   * @return client details
   */
  def findClientDetailsByClientId(clientId: String): ClientDetails

  /**
   * The method register client with specified client details
   * @param clientDetails client details
   * @return client details
   */
  def addClient(clientDetails: ClientDetails): ClientDetails

  /**
   * The method removes client details by client identity
   * @param clientId client identity
   * @return client details
   */
  def removeClient(clientId: String): ClientDetails
}
