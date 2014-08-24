package ai.akka.service.approval

import ai.akka.oauth2.model.AuthorizationRequest
import ai.akka.service.approval.Model.Approval

/**
 * Created by Andrew on 24.08.2014.
 */
trait InMemoryApprovalService extends ApprovalService {

  override def approveAuthorizationRequest(request: AuthorizationRequest): AuthorizationRequest = request

}
