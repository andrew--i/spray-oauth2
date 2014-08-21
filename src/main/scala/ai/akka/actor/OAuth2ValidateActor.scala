package ai.akka.actor

import ai.akka.actor.OAuth2ValidateActor.ValidateAuthorizationRequestMessage
import ai.akka.exception.Exception.{InvalidClientException, UnsupportedResponseTypeException}
import ai.akka.oauth2.Constants
import ai.akka.oauth2.model.AuthorizationRequest
import akka.actor.Actor.Receive
import akka.actor.ActorRef

/**
 * Created by Andrew on 21.08.2014.
 */
class OAuth2ValidateActor extends OAuth2ServiceActor {
  override def receive: Receive = {
    case request: ValidateAuthorizationRequestMessage =>
      val source: ActorRef = sender()
      source ! validateAuthorizationRequest(request.authorizationRequest, request.httpResponseActor)
    case x => sender ! x
  }

  def validateAuthorizationRequest(authorizationRequest: AuthorizationRequest, httpResponseActor: ActorRef): AuthorizationRequest = {
    val responseTypes: Set[String] = authorizationRequest.responseTypes
    if (!responseTypes.contains(Constants.TOKEN_RESPONSE_TYPE) && !responseTypes.contains(Constants.CODE_RESPONSE_TYPE))
      throw new UnsupportedResponseTypeException(httpResponseActor, "Unsupported response types: " + responseTypes)
    if (authorizationRequest.clientId == null || authorizationRequest.clientId.isEmpty)
      throw new InvalidClientException(httpResponseActor, "A client id must be provided")
    authorizationRequest
  }
}

object OAuth2ValidateActor {

  case class ValidateAuthorizationRequestMessage(authorizationRequest: AuthorizationRequest, httpResponseActor: ActorRef)

}
