package ai.akka.oauth2

import akka.http.model.{StatusCode, StatusCodes}

/**
 * Created by Andrew on 26.08.2014.
 */
object Exception {
  final val ERROR: String = "error"
  final val DESCRIPTION: String = "error_description"
  final val URI: String = "error_uri"
  final val INVALID_REQUEST: String = "invalid_request"
  final val INVALID_CLIENT: String = "invalid_client"
  final val INVALID_GRANT: String = "invalid_grant"
  final val UNAUTHORIZED_CLIENT: String = "unauthorized_client"
  final val UNSUPPORTED_GRANT_TYPE: String = "unsupported_grant_type"
  final val INVALID_SCOPE: String = "invalid_scope"
  final val INSUFFICIENT_SCOPE: String = "insufficient_scope"
  final val INVALID_TOKEN: String = "invalid_token"
  final val REDIRECT_URI_MISMATCH: String = "redirect_uri_mismatch"
  final val UNSUPPORTED_RESPONSE_TYPE: String = "unsupported_response_type"
  final val ACCESS_DENIED: String = "access_denied"

  trait OAuth2Exception {
    var additionalInformation: Map[String, String] = Map.empty

    def getOAuth2ErrorCode: String

    def getHttpErrorCode: StatusCode = StatusCodes.BadRequest

    def msg:String
  }

  def apply(errorCode: String, errorMessage: String): OAuth2Exception = {
    require(errorCode != null)
    errorCode match {
      case INVALID_CLIENT => new InvalidOAuth2ClientException(errorMessage)
      case UNAUTHORIZED_CLIENT => new UnauthorizedOAuth2UserException(errorMessage)
      case INVALID_GRANT => new InvalidOAuth2GrantException(errorMessage)
      case INVALID_SCOPE => new InvalidOauth2ScopeException(errorMessage)
      case INVALID_TOKEN => new InvalidOAuth2TokenException(errorMessage)
      case INVALID_REQUEST => new InvalidOAuth2RequestException(errorMessage)
      case REDIRECT_URI_MISMATCH => new InvalidOAuth2RedirectMismatchException(errorMessage)
      case UNSUPPORTED_GRANT_TYPE => new InvalidOAuth2UnsupportedGrantTypeException(errorMessage)
      case UNSUPPORTED_RESPONSE_TYPE => new InvalidOAuth2UnsupportedResponseTypeException(errorMessage)
      case ACCESS_DENIED => new UserDeniedOAuth2AuthorizationException(errorMessage)
      case _ => new DefaultOAuth2Exception(errorMessage)
    }
  }


  case class DefaultOAuth2Exception(msg: String) extends OAuth2Exception {
    override def getOAuth2ErrorCode: String = "invalid_request"
  }


  case class InvalidOAuth2ClientException(msg: String) extends OAuth2Exception {

    override def getOAuth2ErrorCode: String = INVALID_CLIENT

    override def getHttpErrorCode: StatusCode = StatusCodes.Unauthorized
  }

  case class UnauthorizedOAuth2UserException(msg: String) extends OAuth2Exception {
    override def getOAuth2ErrorCode: String = UNAUTHORIZED_CLIENT

    override def getHttpErrorCode: StatusCode = StatusCodes.Unauthorized
  }

  case class InvalidOAuth2GrantException(msg: String) extends OAuth2Exception {
    override def getOAuth2ErrorCode: String = INVALID_GRANT
  }

  case class InvalidOauth2ScopeException(msg: String) extends OAuth2Exception {
    override def getOAuth2ErrorCode: String = INVALID_SCOPE
  }

  case class InvalidOAuth2TokenException(msg: String) extends OAuth2Exception {
    override def getOAuth2ErrorCode: String = INVALID_TOKEN

    override def getHttpErrorCode: StatusCode = StatusCodes.Unauthorized
  }

  case class InvalidOAuth2RequestException(msg: String) extends OAuth2Exception {
    override def getOAuth2ErrorCode: String = INVALID_REQUEST
  }

  case class InvalidOAuth2RedirectMismatchException(msg: String) extends OAuth2Exception {
    override def getOAuth2ErrorCode: String = REDIRECT_URI_MISMATCH
  }

  case class InvalidOAuth2UnsupportedGrantTypeException(msg: String) extends OAuth2Exception {
    override def getOAuth2ErrorCode: String = UNSUPPORTED_GRANT_TYPE
  }

  case class InvalidOAuth2UnsupportedResponseTypeException(msg: String) extends OAuth2Exception {
    override def getOAuth2ErrorCode: String = UNSUPPORTED_RESPONSE_TYPE
  }

  case class UserDeniedOAuth2AuthorizationException(msg: String) extends OAuth2Exception {
    override def getOAuth2ErrorCode: String = ACCESS_DENIED
  }


}
