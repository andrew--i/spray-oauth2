package ai.akka.service.client


object Model {

  /**
   * Information about client
   * @param clientId client identity
   * @param clientSecret client secret
   * @param scope scopes of client
   * @param resourceIds identity of resources
   * @param authorizedGrantTypes registered authorization grant types
   * @param registeredRedirectUris registered redirect uri-s
   * @param autoApproveScopes scopes with auto approved
   * @param authorities client authorities
   * @param accessTokenValiditySeconds  the access token validity period for this client
   * @param refreshTokenValiditySeconds the refresh token validity period for this client
   * @param additionalInformation additional client info
   */
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

  /**
   * Model of authority
   * @param authority name of authority
   */
  case class GrantedAuthority(authority: String)

}
