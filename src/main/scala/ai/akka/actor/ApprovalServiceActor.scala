package ai.akka.actor

import ai.akka.actor.ApprovalServiceActor.ApproveRequestMessage
import ai.akka.oauth2.model.AuthorizationRequest
import ai.akka.service.approval.ApprovalService
import akka.actor.Actor.Receive
import akka.actor.ActorRef

/**
 * Created by Andrew on 24.08.2014.
 */
trait ApprovalServiceActor extends OAuth2ServiceActor with ApprovalService {
  override def receive: Receive = {
    case approveRequestMessage: ApproveRequestMessage =>
      val source: ActorRef = sender()
      source ! approveAuthorizationRequest(approveRequestMessage.request)

  }
}

object ApprovalServiceActor {

  case class ApproveRequestMessage(request: AuthorizationRequest)

}
