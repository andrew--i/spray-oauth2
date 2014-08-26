package ai.akka.oauth2.model

import ai.akka.oauth2.Constants
import ai.akka.service.client.Model.GrantedAuthority

/**
 * Trait with common information about request
 */
trait BaseRequest {
  val clientId: String
  val scope: Set[String]
  val requestParameters: Map[String, String]
}

/**
 * Information about authorization request (request to authorization-endpoint)
 * @param clientId client identity
 * @param scope set of required scopes
 * @param requestParameters parameters of request
 * @param approvalParameters approval parameters
 * @param state state
 * @param responseTypes response types
 * @param resourceIds set of identity of resource for access
 * @param authorities authorities of user
 * @param approved is request approved
 * @param redirectUri redirect uri
 * @param extensions extension of request
 */
case class AuthorizationRequest(
                                 clientId: String,
                                 scope: Set[String],
                                 requestParameters: Map[String, String],
                                 approvalParameters: Map[String, String],
                                 state: String,
                                 responseTypes: Set[String],
                                 resourceIds: Set[String],
                                 authorities: Set[GrantedAuthority],
                                 approved: Boolean,
                                 redirectUri: String,
                                 extensions: Map[String, String]) extends BaseRequest {

  def isContainsTokenResponseType: Boolean = if (responseTypes == null) false else responseTypes.contains(Constants.TOKEN_RESPONSE_TYPE)

  def isContainsCodeResponseType: Boolean = if (responseTypes == null) false else responseTypes.contains(Constants.CODE_RESPONSE_TYPE)

}
