package ai.spray.oauth2.actor.message

/**
 * Created by Andrew on 10.08.2014.
 */
object RequestMessage {
  trait BaseRequest
  case class AuthorizationGetRequestMessage() extends BaseRequest
  case class AuthorizationPostRequestMessage() extends BaseRequest

}
