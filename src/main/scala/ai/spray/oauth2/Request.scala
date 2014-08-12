package ai.spray.oauth2

/**
 * Created by Andrew on 10.08.2014.
 */
object Request {
  trait BaseRequest
  case class AuthorizationGetRequest() extends BaseRequest
  case class AuthorizationPostRequest() extends BaseRequest

}
