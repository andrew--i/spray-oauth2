package ai.spray.oauth2.model

import ai.akka.actor.ClientDetailsServiceActor
import ai.akka.actor.ClientDetailsServiceActor.GrantedAuthority

/**
 * Created by Андрей Смирнов on 14.08.2014.
 */
trait BaseRequest {
  val clientId: String
  val scope: Set[String]
  val requestParameters: Map[String, String]
}

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
}
