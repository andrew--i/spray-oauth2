package ai.akka.actor

import ai.akka.actor.ClientDetailsServiceActor._
import ai.akka.actor.OAuth2RequestFactoryActor.CreateAuthorizationRequestMessage
import ai.akka.exception.Exception.OAuthParseRequestException
import ai.akka.oauth2.Constants
import ai.akka.oauth2.model.AuthorizationRequest
import ai.akka.service.client.Model.ClientDetails
import ai.akka.util.OAuth2Utils
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
        case Success(d: ClientDetails) => source ! AuthorizationRequest(
          request.clientId,
          request.scopes(d),
          request.requestParameters,
          Map.empty,
          request.state,
          Set.empty,
          d.resourceIds,
          d.authorities,
          approved = false,
          request.redirectUri,
          Map.empty)
        case Failure(t: Throwable) => throw t
      }
  }
}

object OAuth2RequestFactoryActor {

  case class CreateAuthorizationRequestMessage(request: HttpRequest, clientDetailsService: ActorRef, httpResponseActor: ActorRef) {

    lazy val requestParameters: Map[String, String] = request.uri.query.toMap

    lazy val clientId: String =
      requestParameters.get(Constants.CLIENT_ID) match {
        case None => throw new OAuthParseRequestException(httpResponseActor, s"${Constants.CLIENT_ID} parameter does not found")
        case Some(x) => x
      }

    lazy val redirectUri: String = requestParameters.getOrElse(Constants.REDIRECT_URI, null)

    lazy val state: String = requestParameters.getOrElse(Constants.STATE, null)

    def scopes(clientDetails: ClientDetails): Set[String] = {
      val scopeInParameters: Set[String] = OAuth2Utils.parseParameterList(requestParameters.getOrElse(Constants.SCOPE, null))
      if (scopeInParameters == null || scopeInParameters.isEmpty) {
        clientDetails.scope
      } else
        scopeInParameters
    }

  }

} 

