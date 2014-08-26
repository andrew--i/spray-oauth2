package ai.akka.service.validate

import ai.akka.exception.Exception.{InvalidAuthenticationException, InvalidClientException, UnsupportedResponseTypeException}
import ai.akka.oauth2.Constants
import ai.akka.oauth2.model.AuthorizationRequest
import ai.akka.service.authentication.Model.Authentication
import akka.actor.ActorRef

/**
 * Trait of implementation validation service
 */
trait OAuth2ValidateRequestService extends OAuth2ValidateService {

  /**
   * @see(ai.akka.service.validate.OAuth2ValidateService)
   * @param authorizationRequest authorization request
   * @param authentication authentication
   * @param httpResponseActor regerence to actor with waiting http response
   * @return authorization request
   */
  def validateAuthorizationRequest(authorizationRequest: AuthorizationRequest, authentication: Authentication, httpResponseActor: ActorRef): AuthorizationRequest = {
    if (authorizationRequest == null)
      throw new IllegalArgumentException("Authorization request did not specified")
    val responseTypes: Set[String] = authorizationRequest.responseTypes
    if (responseTypes == null || !authorizationRequest.isContainsTokenResponseType && !authorizationRequest.isContainsCodeResponseType)
      throw new UnsupportedResponseTypeException(httpResponseActor, "Unsupported response types: " + responseTypes)
    if (authorizationRequest.clientId == null || authorizationRequest.clientId.isEmpty)
      throw new InvalidClientException(httpResponseActor, "A client id must be provided")
    if (authentication == null || !authentication.isAuthenticated)
      throw new InvalidAuthenticationException(httpResponseActor, "User is not authenticated")
    authorizationRequest
  }
}
