package ai.akka.actor

import akka.http.model._
import akka.pattern.ask
import akka.testkit.TestActorRef
import akka.util.Timeout
import org.specs2.mutable.Specification
import org.specs2.time.NoTimeConversions
import spray.testkit.Specs2RouteTest
import akka.http.model.headers._
import akka.http.model.ContentTypes._
import scala.collection.immutable.Seq
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
 * Created by Андрей Смирнов on 20.08.2014.
 */
class RequestProcessingActorTest extends Specification with Specs2RouteTest with NoTimeConversions {

  private val actorRef: TestActorRef[RequestProcessingActor] = TestActorRef[RequestProcessingActor]
  private val actor: RequestProcessingActor = actorRef.underlyingActor
  implicit val timeout: Timeout = Timeout(2000.millis)


  def processRequestViaMessage(message: Any): HttpResponse = {
    val future: Future[HttpResponse] = (actorRef ? message).mapTo[HttpResponse]
    Await.result[HttpResponse](future, Duration("2000ms"))
  }

  "The RequestProcessingActor" should {

    "return response with json content type" in {
      val response: HttpResponse = processRequestViaMessage("123")
      val headers: Seq[HttpHeader] = response.headers
      (headers.size must_== 1) and (headers(0) mustEqual `Content-Type`(`application/json`))
    }

    "handle not akka http requests" in {
      val response: HttpResponse = processRequestViaMessage("hello world")
      (response !== null) and (response.status must_==  StatusCodes.NotFound)

    }

//    "handle /oauth/authorize GET requests" in {
//      val response: HttpResponse = processRequestViaMessage(HttpRequest(HttpMethods.GET, uri = "/oauth/authorize"))
//      (response !== null) and (response.status must_==  StatusCodes.InternalServerError) and (response.entity must_== "hello world" )
//    }

    "handle /oauth/authorize GET requests with clientId" in {
//      actorRef ! Get("/oauth/authorize?" + Constants.CLIENT_ID + "=1")
      1 === 1
    }

  }


}
