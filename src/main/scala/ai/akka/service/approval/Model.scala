package ai.akka.service.approval

import java.util.Date

import ai.akka.service.approval.Model.ApprovalStatus.ApprovalStatus


object Model {

  /**
   * Information about approval
   * @param userId user identity
   * @param clientId client identiry
   * @param scope scope
   * @param status status of approval
   * @param expiresAt date and time when approval expires
   * @param lastUpdatedAt date and time when approval has updated
   */
  case class Approval(
                       userId: String,
                       clientId: String,
                       scope: String,
                       status: ApprovalStatus,
                       expiresAt: Date,
                       lastUpdatedAt: Date)

  /**
   * Enumeration of approval status
   */
  object ApprovalStatus extends Enumeration {
    type ApprovalStatus = Value
    val APPROVED, DENIED = Value
  }

}
