import ai.spray.service.OAuth2Service
import akka.testkit.TestActorRef
import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http.StatusCodes._

/**
 * Created by Andrew on 13.08.2014.
 */
class OAuth2ServiceTest extends Specification with Specs2RouteTest {

  val oauth2ServiceRef = TestActorRef[OAuth2Service]
  val oauth2Service: OAuth2Service = oauth2ServiceRef.underlyingActor

  "The OAuth2Service service" should {
    "return empty response" in {
      Get("/oauth/authorize") ~> oauth2Service.oauth2Route ~> check {
        responseAs[String] mustEqual "[]"
      }
    }

    "return internal server error" in {
      Post("/oauth/authorize") ~> oauth2Service.oauth2Route ~> check {
        status === InternalServerError
      }
    }
  }
}
