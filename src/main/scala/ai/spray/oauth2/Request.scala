package ai.spray.oauth2

/**
 * Created by Andrew on 10.08.2014.
 */
object Request {
  trait BaseRequest
  case class AuthorizationRequest() extends BaseRequest

}
