package ai.akka.service.validate

import ai.akka.oauth2.model.AuthorizationRequest
import ai.akka.service.authentication.Model.Authentication
import akka.actor.ActorRef

/**
 * Trait of validate authorization request
 */
trait OAuth2ValidateService {
  /**
   * The method validate authorization request and authentication
   * @param authorizationRequest authorization request
   * @param authentication authentication
   * @param httpResponseActor regerence to actor with waiting http response
   * @return authorization request
   */
  def validateAuthorizationRequest(authorizationRequest: AuthorizationRequest, authentication: Authentication, httpResponseActor: ActorRef): AuthorizationRequest
}
