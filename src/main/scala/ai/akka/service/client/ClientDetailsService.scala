package ai.akka.service.client

import ai.akka.service.client.Model.ClientDetails

/**
 * Created by andrew on 23.08.14.
 */

trait ClientDetailsService {
  def findClientDetailsByClientId(clientId: String): ClientDetails

  def addClient(clientDetails: ClientDetails): ClientDetails

  def removeClient(clientId: String): ClientDetails
}
