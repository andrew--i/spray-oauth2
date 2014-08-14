package ai.spray.oauth2

/**
 * Created by Andrew on 10.08.2014.
 */
object Exception {

  trait OAuthException extends Exception

  case class OAuthParseRequestException(message:String) extends OAuthException
}
