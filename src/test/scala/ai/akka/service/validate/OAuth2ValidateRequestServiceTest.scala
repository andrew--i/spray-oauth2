package ai.akka.service.validate

import ai.akka.exception.Exception.{UnsupportedResponseTypeException, InvalidClientException}
import ai.akka.oauth2.Constants
import ai.akka.oauth2.model.AuthorizationRequest
import org.specs2.mutable.Specification

/**
 * Created by Andrew on 24.08.2014.
 */
class OAuth2ValidateRequestServiceTest extends Specification{
  object OAuth2ValidateRequestServiceForTest extends OAuth2ValidateRequestService

  def authorizationRequest(clientId:String, responseTypes:Set[String]) = {
    AuthorizationRequest(clientId, Set.empty, Map.empty, Map.empty, "", responseTypes, Set.empty, Set.empty, approved = false, "", Map.empty)
  }

  "The OAuth2ValidateRequestService" should {
    "pass authorization request with response_type and client_id" in {
      val request: AuthorizationRequest = authorizationRequest("clientId", Set(Constants.TOKEN_RESPONSE_TYPE))
      OAuth2ValidateRequestServiceForTest.validateAuthorizationRequest(request, null) must be equalTo request
    }

    "fail null authorization request" in {
      OAuth2ValidateRequestServiceForTest.validateAuthorizationRequest(null, null) must throwA[IllegalArgumentException]
    }

    "fail authorization request without client_id " in {
      val request: AuthorizationRequest = authorizationRequest("", Set(Constants.TOKEN_RESPONSE_TYPE))
      OAuth2ValidateRequestServiceForTest.validateAuthorizationRequest(request, null) must throwA[InvalidClientException]
    }

    "fail authorization request with null response types" in {
      val request: AuthorizationRequest = authorizationRequest("clientId", null)
      OAuth2ValidateRequestServiceForTest.validateAuthorizationRequest(request, null) must throwA[UnsupportedResponseTypeException]
    }

    "fail authorization request with empty response types" in {
      val request: AuthorizationRequest = authorizationRequest("clientId", Set.empty)
      OAuth2ValidateRequestServiceForTest.validateAuthorizationRequest(request, null) must throwA[UnsupportedResponseTypeException]
    }

    "fail authorization request without specified response types" in {
      val request: AuthorizationRequest = authorizationRequest("clientId", Set("123"))
      OAuth2ValidateRequestServiceForTest.validateAuthorizationRequest(request, null) must throwA[UnsupportedResponseTypeException]
    }
  }

}
