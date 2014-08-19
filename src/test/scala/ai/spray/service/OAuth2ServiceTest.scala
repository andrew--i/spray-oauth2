package ai.spray.service

import akka.testkit.TestActorRef
import org.specs2.mutable.Specification
import spray.http.StatusCodes._
import spray.testkit.Specs2RouteTest

/**
 * Created by Andrew on 13.08.2014.
 */
class OAuth2ServiceTest extends Specification with Specs2RouteTest {

  val oauth2ServiceRef = TestActorRef[OAuth2ServiceActor]
  val oauth2Service: OAuth2ServiceActor = oauth2ServiceRef.underlyingActor

  "The OAuth2Service service" should {
    "return empty response" in {
      Get("/oauth/authorize") ~> oauth2Service.oauth2Route ~> check {
        status === OK
      }
    }

    "return internal server error" in {
      Post("/oauth/authorize") ~> oauth2Service.oauth2Route ~> check {
        status === InternalServerError
      }
    }
  }
}
