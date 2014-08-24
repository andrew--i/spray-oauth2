package ai.akka.service.approval

import ai.akka.oauth2.model.AuthorizationRequest

/**
 * Created by Andrew on 24.08.2014.
 */
trait ApprovalService {
  def approveAuthorizationRequest(request: AuthorizationRequest): AuthorizationRequest
}
