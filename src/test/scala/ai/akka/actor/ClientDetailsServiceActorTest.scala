package ai.akka.actor

import ai.akka.actor.ClientDetailsServiceActor.LoadClientByClientIdMessage
import ai.akka.actor.ClientDetailsServiceActorTest.DumbClientDetailsServiceActor
import ai.akka.service.client.ClientDetailsService
import ai.akka.service.client.Model.ClientDetails
import akka.actor.Props
import akka.testkit.TestActorRef

/**
 * Created by andrew on 23.08.14.
 */
class ClientDetailsServiceActorTest  extends ActorTestWithFutureResponse {

  "The ClientDetailsServiceActor" should {
    "respond to LoadClientByClientIdMessage " in {
      val actorRef: TestActorRef[Nothing] = TestActorRef(Props(new DumbClientDetailsServiceActor()))
      val message: LoadClientByClientIdMessage = LoadClientByClientIdMessage("1")
      val details: ClientDetails = getResponseFromActor[ClientDetails](actorRef, message)
      details must not beNull
    }
  }

}

object ClientDetailsServiceActorTest {

  trait DumbClientDetailsService extends ClientDetailsService {
    override def loadClientDetailsByClientId(clientId: String): ClientDetails = ClientDetails(clientId)
  }

  class DumbClientDetailsServiceActor extends ClientDetailsServiceActor with DumbClientDetailsService


}
