package ai.akka.service.approval

import ai.akka.oauth2.model.AuthorizationRequest

/**
 * Trait with implement in-memory storage for approvals
 */
trait InMemoryApprovalService extends ApprovalService {

  override def approveAuthorizationRequest(request: AuthorizationRequest): AuthorizationRequest = request

}
