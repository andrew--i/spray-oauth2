package ai.akka.actor

import ai.akka.actor.ClientDetailsServiceActor.{ClientDetails, LoadClientByClientIdMessage}

/**
 * Created by Андрей Смирнов on 20.08.2014.
 */
trait ClientDetailsServiceActor extends OAuth2ServiceActor with ClientDetailsService {
  override def receive: Receive = {
    case LoadClientByClientIdMessage(clientId) => sender ! loadClientDetailsByClientId(clientId)
  }
}

trait ClientDetailsService {
  def loadClientDetailsByClientId(clientId: String): ClientDetails
}

object ClientDetailsServiceActor {

  case class LoadClientByClientIdMessage(clientId: String)

  case class ClientDetails(
                            clientId: String,
                            clientSecret: String,
                            scope: Set[String],
                            resourceIds: Set[String],
                            authorizedGrantTypes: Set[String],
                            registeredRedirectUris: Set[String],
                            autoApproveScopes: Set[String],
                            authorities: Set[GrantedAuthority],
                            accessTokenValiditySeconds: Int,
                            refreshTokenValiditySeconds: Int,
                            additionalInformation: Map[String, Any])

  object ClientDetails {
    def apply(clientId: String): ClientDetails = {
      ClientDetails(clientId, "", Set.empty, Set.empty, Set.empty, Set.empty, Set.empty, Set.empty, 0, 0, Map.empty)
    }
  }

  case class GrantedAuthority(authority: String)

}
