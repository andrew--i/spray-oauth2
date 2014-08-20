package ai.akka.actor

import ai.spray.oauth2.Constants
import akka.actor.Props
import akka.http.model._
import org.specs2.mutable.Specification
import org.specs2.time.NoTimeConversions

import scala.concurrent.duration._

/**
 * Created by Андрей Смирнов on 20.08.2014.
 */
class RequestProcessingActorTest extends Specification with NoTimeConversions {
  "The RequestProcessingActor" should {
    "handle /oauth/authorize GET requests" in new AkkaTestkitSpecs2Support {
      within(500.millis) {
        system.actorOf(Props[RequestProcessingActor]) ! HttpRequest(HttpMethods.GET, uri = "/oauth/authorize")
        expectMsgType[HttpResponse] must not beNull
      }
    }

    "handle /oauth/authorize GET requests with clientId and responseType parameters" in new AkkaTestkitSpecs2Support {
      within(500.millis) {
        system.actorOf(Props[RequestProcessingActor]) ! HttpRequest(HttpMethods.GET, uri = "/oauth/authorize?" + Constants.CLIENT_ID + "=123&" + Constants.RESPONSE_TYPE + "=resp_type")
        expectMsg[HttpResponse](HttpResponse(StatusCodes.OK))
      }
    }

  }


}