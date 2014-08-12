package ai.spray.service.authorize

import ai.spray.oauth2.Request.{AuthorizationGetRequest, AuthorizationPostRequest, BaseRequest}
import ai.spray.service.PerRequestCreator
import akka.actor.{Actor, Props}
import spray.http.StatusCodes._
import spray.routing._

/**
 * Created by Andrew on 10.08.2014.
 * Обработчик endpoint-авторизации
 */
class AuthorizeServiceActor(requestContext: RequestContext) extends Actor {
  override def receive: Receive = {
    case getRequest: AuthorizationGetRequest => context.parent ! (requestContext.request.headers, OK)
    case postRequest: AuthorizationPostRequest => throw new NotImplementedError("AuthorizationPostRequest не реализована обработка");
  }
}

trait AuthorizeService extends Actor with PerRequestCreator {
  def authorizeClient(request: BaseRequest): Route = {
    ctx => perRequest(ctx, Props(new AuthorizeServiceActor(ctx)), request);
  }
}
