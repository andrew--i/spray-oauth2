package ai.akka.service.client

import ai.akka.service.client.Model.ClientDetails
import org.specs2.mutable.Specification

/**
 * Created by Andrew on 24.08.2014.
 */
class InMemoryClientDetailsServiceTest extends Specification {

  object InMemoryClientDetailsServiceForTest extends InMemoryClientDetailsService

  "The InMemoryClientDetailsService" should {
    "register new client" in {
      val client: ClientDetails = InMemoryClientDetailsServiceForTest.addClient(ClientDetails("1"))
      (client must not beNull) and (client.clientId must be equalTo  "1")
    }

    "return clientDetails if client has registration" in {
      val client: ClientDetails = InMemoryClientDetailsServiceForTest.addClient(ClientDetails("1"))
      InMemoryClientDetailsServiceForTest.findClientDetailsByClientId("1") must be equalTo client
    }

    "throw exception if specified client did not find" in {
      InMemoryClientDetailsServiceForTest.findClientDetailsByClientId("12") must throwA[IllegalArgumentException]
    }

    "remove client" in {
      val client: ClientDetails = InMemoryClientDetailsServiceForTest.addClient(ClientDetails("1"))
      InMemoryClientDetailsServiceForTest.removeClient("1") must be equalTo client
    }

    "throw exception when tries remove client with no registration" in {
      InMemoryClientDetailsServiceForTest.removeClient("1") must throwA[IllegalArgumentException]
    }
  }

}
