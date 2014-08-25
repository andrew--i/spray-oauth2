package ai.akka.service.approval

import ai.akka.oauth2.model.AuthorizationRequest

/**
 * Trait of approval service
 */
trait ApprovalService {
  /**
   * The method tries to approve authorization request
   * @param request authorization request
   * @return authorization request
   */
  def approveAuthorizationRequest(request: AuthorizationRequest): AuthorizationRequest
}
