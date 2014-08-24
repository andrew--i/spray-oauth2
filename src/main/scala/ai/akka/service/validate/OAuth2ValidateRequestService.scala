package ai.akka.service.validate

import ai.akka.exception.Exception.{InvalidClientException, UnsupportedResponseTypeException}
import ai.akka.oauth2.Constants
import ai.akka.oauth2.model.AuthorizationRequest
import akka.actor.ActorRef

/**
 * Created by andrew on 23.08.14.
 */
trait OAuth2ValidateRequestService extends OAuth2ValidateService {

  def validateAuthorizationRequest(authorizationRequest: AuthorizationRequest, httpResponseActor: ActorRef): AuthorizationRequest = {
    if(authorizationRequest == null)
      throw new IllegalArgumentException("Authorization request did not specified")
    val responseTypes: Set[String] = authorizationRequest.responseTypes
    if (responseTypes == null || !responseTypes.contains(Constants.TOKEN_RESPONSE_TYPE) && !responseTypes.contains(Constants.CODE_RESPONSE_TYPE))
      throw new UnsupportedResponseTypeException(httpResponseActor, "Unsupported response types: " + responseTypes)
    if (authorizationRequest.clientId == null || authorizationRequest.clientId.isEmpty)
      throw new InvalidClientException(httpResponseActor, "A client id must be provided")
    authorizationRequest
  }
}
