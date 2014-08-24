package ai.akka.service.client

/**
 * Created by andrew on 23.08.14.
 */
object Model {

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
