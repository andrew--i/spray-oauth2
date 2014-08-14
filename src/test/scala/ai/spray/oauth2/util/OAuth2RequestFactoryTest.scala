package ai.spray.oauth2.util

import ai.spray.oauth2.Exception.OAuthParseRequestException
import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest

/**
 * Created by Андрей Смирнов on 14.08.2014.
 */
class OAuth2RequestFactoryTest extends Specification with Specs2RouteTest {


  "The OAuth2RequestFactory when create authorization request" should {
    "raise exception when he works with null request " in {
      OAuth2RequestFactory.createAuthorizationRequest(null) must throwA[IllegalArgumentException]
    }

    "raise exception if clientId parameter does not exist" in {
      OAuth2RequestFactory.createAuthorizationRequest(Get("/?redirectUri=1")) must throwA[OAuthParseRequestException]
    }

    "raise exception if redirectUri parameter does not exist" in {
      OAuth2RequestFactory.createAuthorizationRequest(Get("/?clientId=1")) must throwA[OAuthParseRequestException]
    }
  }

  "The OAuth2RequestFactory when create request parameters map " should {
    "raise exception when he works with null request" in {
      OAuth2RequestFactory.createRequestParamsMap(null) must throwA[IllegalArgumentException]
    }

    "return empty map when he works with request without params" in {
      OAuth2RequestFactory.createRequestParamsMap(Get("/")) must empty
    }

    "return all query params" in {
      OAuth2RequestFactory.createRequestParamsMap(Get("/?hello=world&hi=andrew")) mustEqual Map[String, String]("hello" -> "world", "hi" -> "andrew")
    }
  }
}
