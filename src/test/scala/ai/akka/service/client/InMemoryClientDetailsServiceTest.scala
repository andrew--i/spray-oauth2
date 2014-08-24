package ai.akka.service.client

import ai.akka.service.client.Model.ClientDetails
import org.specs2.mutable.Specification

/**
 * Created by Andrew on 24.08.2014.
 */
class InMemoryClientDetailsServiceTest extends Specification {

  class InMemoryClientDetailsServiceForTest extends InMemoryClientDetailsService

  "The InMemoryClientDetailsService" should {
    "register new client" in {
      val serviceForTest: InMemoryClientDetailsServiceForTest = new InMemoryClientDetailsServiceForTest()
      val client: ClientDetails = serviceForTest.addClient(ClientDetails("1"))
      (client must not beNull) and (client.clientId must be equalTo  "1")
    }

    "return clientDetails if client has registration" in {
      val serviceForTest: InMemoryClientDetailsServiceForTest = new InMemoryClientDetailsServiceForTest()
      val client: ClientDetails = serviceForTest.addClient(ClientDetails("1"))
      serviceForTest.findClientDetailsByClientId("1") must be equalTo client
    }

    "throw exception if specified client did not find" in {
      val serviceForTest: InMemoryClientDetailsServiceForTest = new InMemoryClientDetailsServiceForTest()
      serviceForTest.findClientDetailsByClientId("12") must throwA[IllegalArgumentException]
    }

    "remove client" in {
      val serviceForTest: InMemoryClientDetailsServiceForTest = new InMemoryClientDetailsServiceForTest()
      val client: ClientDetails = serviceForTest.addClient(ClientDetails("1"))
      serviceForTest.removeClient("1") must be equalTo client
    }

    "throw exception when tries remove client with no registration" in {
      val serviceForTest: InMemoryClientDetailsServiceForTest = new InMemoryClientDetailsServiceForTest()
      serviceForTest.removeClient("1") must throwA[IllegalArgumentException]
    }
  }

}
