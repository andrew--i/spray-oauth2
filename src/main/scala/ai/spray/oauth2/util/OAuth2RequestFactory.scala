package ai.spray.oauth2.util

import ai.spray.oauth2.Constants
import ai.spray.oauth2.Exception.OAuthParseRequestException
import ai.spray.oauth2.model.AuthorizationRequest
import spray.http.HttpRequest

/**
 * Created by Андрей Смирнов on 14.08.2014.
 */
object OAuth2RequestFactory {
  def createAuthorizationRequest(request: HttpRequest): AuthorizationRequest = {
    require(request != null)
    val requestParams: Map[String, String] = createRequestParamsMap(request)
    val clientId: String =
      requestParams.get(Constants.CLIENT_ID) match {
        case None => throw new OAuthParseRequestException(s"${Constants.CLIENT_ID} parameter does not found")
        case Some(x) => x
      }
    val redirectUri: String = requestParams.get(Constants.REDIRECT_URI) match {
      case Some(x) => x
      case None => throw new OAuthParseRequestException(s"${Constants.REDIRECT_URI} parameter does not found")
    }

    AuthorizationRequest(clientId, Set.empty, Map.empty, Map.empty, "state", Set.empty, Set.empty, Set.empty, approved = false, redirectUri, Map.empty)
  }

  def createRequestParamsMap(request: HttpRequest): Map[String, String] = {
    require(request != null)
    request.uri.query.toMap
  }
}

