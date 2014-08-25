package ai.akka.service.validate

import ai.akka.oauth2.model.AuthorizationRequest
import ai.akka.service.authentication.Model.Authentication
import akka.actor.ActorRef

/**
 * Created by andrew on 23.08.14.
 */
trait OAuth2ValidateService {
  def validateAuthorizationRequest(authorizationRequest: AuthorizationRequest, authentication:Authentication,httpResponseActor: ActorRef): AuthorizationRequest
}
