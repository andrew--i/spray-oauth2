package ai.spray.service.authorize

import ai.spray.oauth2.actor.message.RequestMessage.{AuthorizationGetRequestMessage, AuthorizationPostRequestMessage, BaseRequest}
import ai.spray.service.OAuth2Service
import akka.actor.{Actor, ActorRef, Props}
import spray.http.StatusCodes

/**
 * Created by Andrew on 10.08.2014.
 * Обработчик endpoint-авторизации
 */
class AuthorizeServiceActor() extends OAuth2Service {

  override def receive: Receive = {
    case getRequest: AuthorizationGetRequestMessage =>
      completeRequest(getRequest, StatusCodes.OK, "It`s works");
    case postRequest: AuthorizationPostRequestMessage =>
      throw_(new NotImplementedError("AuthorizationPostRequest не реализована обработка"), postRequest.ctx)
    case _ => throw new IllegalArgumentException("")
  }

}


trait AuthorizeService extends Actor {
  val authorizeActor: ActorRef = context.actorOf(Props[AuthorizeServiceActor])

  def authorizeClient(request: BaseRequest): Unit = {
    authorizeActor ! request
  }
}
