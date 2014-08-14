package ai.spray.service.authorize

import ai.spray.oauth2.actor.message.RequestMessage.{AuthorizationGetRequestMessage, AuthorizationPostRequestMessage, BaseRequest}
import ai.spray.service.PerRequestCreator
import akka.actor.{Actor, Props}
import spray.routing._

/**
 * Created by Andrew on 10.08.2014.
 * Обработчик endpoint-авторизации
 */
class AuthorizeServiceActor(requestContext: RequestContext) extends Actor {
  override def receive: Receive = {
    case getRequest: AuthorizationGetRequestMessage => context.parent ! "It`s works"
    case postRequest: AuthorizationPostRequestMessage => throw new NotImplementedError("AuthorizationPostRequest не реализована обработка");
  }

}

trait AuthorizeService extends Actor with PerRequestCreator {
  def authorizeClient(request: BaseRequest): Route = {
    ctx => perRequest(ctx, Props(new AuthorizeServiceActor(ctx)), request);
  }
}
