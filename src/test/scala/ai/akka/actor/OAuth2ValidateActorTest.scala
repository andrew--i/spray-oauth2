package ai.akka.actor

import ai.akka.actor.OAuth2ValidateActor.ValidateAuthorizationRequestMessage
import ai.akka.actor.OAuth2ValidateActorTest.SkipOAuth2RequestValidateServiceActor
import ai.akka.oauth2.model.AuthorizationRequest
import ai.akka.service.validate.OAuth2ValidateService
import akka.actor.{Props, ActorRef}
import akka.testkit.TestActorRef

/**
 * Created by andrew on 23.08.14.
 */
class OAuth2ValidateActorTest extends ActorTestWithFutureResponse{




  "The OAuth2ValidateActorTest" should {
    "respond to ValidateAuthorizationRequestMessage" in {
      val r:AuthorizationRequest = AuthorizationRequest("", Set.empty, Map.empty, Map.empty, "", Set.empty, Set.empty, Set.empty, approved = false, "", Map.empty)
      val message: ValidateAuthorizationRequestMessage = ValidateAuthorizationRequestMessage(r, null)
      val actorRef: TestActorRef[Nothing] = TestActorRef(Props(new SkipOAuth2RequestValidateServiceActor))
      val request: AuthorizationRequest = getResponseFromActor[AuthorizationRequest](actorRef, message)
      request mustEqual r
    }
  }

}
object OAuth2ValidateActorTest {
  trait SkipOAuth2RequestValidateService extends OAuth2ValidateService {
    override def validateAuthorizationRequest(authorizationRequest: AuthorizationRequest, httpResponseActor: ActorRef): AuthorizationRequest = authorizationRequest
  }

  class SkipOAuth2RequestValidateServiceActor extends OAuth2ValidateActor with SkipOAuth2RequestValidateService
}
