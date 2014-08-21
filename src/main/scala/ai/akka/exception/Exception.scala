package ai.akka.exception

import akka.actor.ActorRef

/**
 * Created by Андрей Смирнов on 20.08.2014.
 */
object Exception {

  trait OAuthServiceException {
    val httpResponseActor: ActorRef
    val msg: String
  }

  case class OAuthParseRequestException(httpResponseActor: ActorRef, msg: String) extends Throwable with OAuthServiceException {
    override def getMessage: String = msg
  }

}
