package ai.akka.service.client

import ai.akka.service.client.Model.ClientDetails

/**
 * Created by andrew on 23.08.14.
 */

trait ClientDetailsService {
  def loadClientDetailsByClientId(clientId: String): ClientDetails
}
