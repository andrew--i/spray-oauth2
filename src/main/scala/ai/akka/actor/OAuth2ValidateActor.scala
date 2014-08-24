package ai.akka.actor

import ai.akka.actor.OAuth2ValidateActor.ValidateAuthorizationRequestMessage
import ai.akka.oauth2.model.AuthorizationRequest
import ai.akka.service.validate.OAuth2ValidateService
import akka.actor.ActorRef

/**
 * Created by Andrew on 21.08.2014.
 */
trait OAuth2ValidateActor extends OAuth2ServiceActor with OAuth2ValidateService{
  override def receive: Receive = {
    case request: ValidateAuthorizationRequestMessage =>
      val source: ActorRef = sender()
      source ! validateAuthorizationRequest(request.authorizationRequest, request.httpResponseActor)
  }
}

object OAuth2ValidateActor {
  case class ValidateAuthorizationRequestMessage(authorizationRequest: AuthorizationRequest, httpResponseActor: ActorRef)
}
