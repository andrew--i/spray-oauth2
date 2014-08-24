package ai.akka.service.approval

import java.util.Date

import ai.akka.service.approval.Model.ApprovalStatus.ApprovalStatus

/**
 * Created by Andrew on 24.08.2014.
 */
object Model {

  case class Approval(
                       userId: String,
                       clientId: String,
                       scope: String,
                       status: ApprovalStatus,
                       expiresAt: Date,
                       lastUpdatedAt: Date)

  object ApprovalStatus extends Enumeration {
    type ApprovalStatus = Value
    val APPROVED, DENIED = Value
  }

}
