package ai.akka.actor

import ai.akka.actor.ApprovalServiceActor.ApproveRequestMessage
import ai.akka.oauth2.model.AuthorizationRequest
import ai.akka.service.approval.ApprovalService
import akka.actor.Actor.Receive
import akka.actor.ActorRef

/**
 * Actor for approve authorization request
 */
trait ApprovalServiceActor extends OAuth2ServiceActor with ApprovalService {
  override def receive: Receive = {
    case approveRequestMessage: ApproveRequestMessage =>
      val source: ActorRef = sender()
      source ! approveAuthorizationRequest(approveRequestMessage.request)

  }
}

object ApprovalServiceActor {

  /**
   * Message for ApprovalServiceActor
   * @param request request for approve
   */
  case class ApproveRequestMessage(request: AuthorizationRequest)

}
