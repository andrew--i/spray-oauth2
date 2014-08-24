package ai.akka.actor

import ai.akka.actor.AuthenticationServiceActor.AuthenticateUserMessage
import ai.akka.service.authentication.AuthenticationService
import akka.actor.Actor.Receive
import akka.actor.ActorRef
import akka.http.model.HttpRequest

/**
 * Created by Andrew on 24.08.2014.
 */
trait AuthenticationServiceActor extends OAuth2ServiceActor with AuthenticationService {
  override def receive: Receive = {
    case authenticateMessage: AuthenticateUserMessage =>
      val source: ActorRef = sender()
      source ! authenticateUser(authenticateMessage.request)
  }
}

object AuthenticationServiceActor {

  case class AuthenticateUserMessage(request: HttpRequest)
}
