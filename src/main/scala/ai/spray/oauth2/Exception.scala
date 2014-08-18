package ai.spray.oauth2

import spray.routing.RequestContext

/**
 * Created by Andrew on 10.08.2014.
 */
object Exception {

  trait OAuthException extends Exception

  case class OAuthParseRequestException(message: String) extends OAuthException

  case class RequestProcessException(e: Throwable, ctx: RequestContext) extends Throwable {
    override def getMessage: String = e.getMessage
  }

}
