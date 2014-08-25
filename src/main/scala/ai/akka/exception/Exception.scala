package ai.akka.exception

import akka.actor.ActorRef

/**
 * Object with exceptions
 */
object Exception {

  /**
   * Trait with information and reference to actor which waiting http response
   */
  trait OAuthServiceException {
    val httpResponseActor: ActorRef
    val msg: String
  }

  /**
   * Exception when parsing authorization request
   * @param httpResponseActor reference to actor which waiting http response
   * @param msg readable message
   */
  case class OAuthParseRequestException(httpResponseActor: ActorRef, msg: String) extends Throwable with OAuthServiceException {
    override def getMessage: String = msg
  }

  /**
   * Exception when unsupported response type
   * @param httpResponseActor reference to actor which waiting http response
   * @param msg readable message
   */
  case class UnsupportedResponseTypeException(httpResponseActor: ActorRef, msg: String) extends Throwable with OAuthServiceException {
    override def getMessage: String = msg
  }

  /**
   * Exception when client identity is invalid
   * @param httpResponseActor reference to actor which waiting http response
   * @param msg readable message
   */
  case class InvalidClientException(httpResponseActor: ActorRef, msg: String) extends Throwable with OAuthServiceException {
    override def getMessage: String = msg
  }

  /**
   * Exception when authentication is invalid
   * @param httpResponseActor reference to actor which waiting http response
   * @param msg readable message
   */
  case class InvalidAuthenticationException(httpResponseActor: ActorRef, msg: String) extends Throwable with OAuthServiceException {
    override def getMessage: String = msg
  }


}
