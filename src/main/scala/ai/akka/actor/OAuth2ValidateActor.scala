package ai.akka.actor

import ai.akka.actor.OAuth2ValidateActor.ValidateAuthorizationRequestMessage
import ai.akka.oauth2.model.AuthorizationRequest
import ai.akka.service.authentication.Model.Authentication
import ai.akka.service.validate.OAuth2ValidateService
import akka.actor.ActorRef

/**
 * Actor for validating authorization request and user authentication
 */
trait OAuth2ValidateActor extends OAuth2ServiceActor with OAuth2ValidateService{
  override def receive: Receive = {
    case request: ValidateAuthorizationRequestMessage =>
      val source: ActorRef = sender()
      source ! validateAuthorizationRequest(request.authorizationRequest, request.authentication, request.httpResponseActor)
  }
}

object OAuth2ValidateActor {

  /**
   * Message for OAuth2ValidateActor
   * @param authorizationRequest request for validation
   * @param authentication authentication for validation
   * @param httpResponseActor reference to actor which waiting http response
   */
  case class ValidateAuthorizationRequestMessage(authorizationRequest: AuthorizationRequest, authentication:Authentication, httpResponseActor: ActorRef)
}
