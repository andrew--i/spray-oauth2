package ai.akka.actor

import ai.akka.actor.ClientDetailsServiceActor._
import ai.akka.actor.OAuth2RequestFactoryActor.CreateAuthorizationRequestMessage
import ai.akka.exception.Exception.OAuthParseRequestException
import ai.spray.oauth2.Constants
import ai.spray.oauth2.model.AuthorizationRequest
import akka.actor.ActorRef
import akka.http.model.HttpRequest
import akka.pattern.ask

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * Created by Андрей Смирнов on 20.08.2014.
 */
class OAuth2RequestFactoryActor extends OAuth2ServiceActor {

  override def receive: Receive = {
    case request: CreateAuthorizationRequestMessage =>
      val source: ActorRef = sender()
      val eventualDetails: Future[ClientDetails] = (request.clientDetailsService ? LoadClientByClientIdMessage(request.clientId))
        .mapTo[ClientDetails]
      eventualDetails onComplete {
        case Success(d: ClientDetails) => source ! AuthorizationRequest(request.clientId, Set.empty, Map.empty, Map.empty, "state", Set.empty, Set.empty, Set.empty, approved = false, request.redirectUri, Map.empty)
        case Failure(t: Throwable) => throw t
      }
  }
}

object OAuth2RequestFactoryActor {

  case class CreateAuthorizationRequestMessage(request: HttpRequest, clientDetailsService: ActorRef) {
    def clientId: String =
      createRequestParamsMap(request).get(Constants.CLIENT_ID) match {
        case None => throw new OAuthParseRequestException(s"${Constants.CLIENT_ID} parameter does not found")
        case Some(x) => x
      }

    def redirectUri: String =
      createRequestParamsMap(request).get(Constants.REDIRECT_URI) match {
        case Some(x) => x
        case None => throw new OAuthParseRequestException(s"${Constants.REDIRECT_URI} parameter does not found")
      }
  }

  private def createRequestParamsMap(request: HttpRequest): Map[String, String] = {
    require(request != null)
    request.uri.query.toMap
  }
} 

