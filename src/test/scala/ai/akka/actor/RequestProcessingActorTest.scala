package ai.akka.actor

import ai.akka.actor.ClientDetailsServiceActorTest.DumbClientDetailsServiceActor
import ai.akka.actor.OAuth2ValidateActorTest.SkipOAuth2RequestValidateServiceActor
import akka.actor.{ActorRef, Props}
import akka.http.model.ContentTypes._
import akka.http.model._
import akka.http.model.headers._
import akka.testkit.TestActorRef

import scala.collection.immutable.Seq

/**
 * Created by Андрей Смирнов on 20.08.2014.
 */


class RequestProcessingActorTest extends ActorTestWithFutureResponse {

  "The RequestProcessingActor" should {

    "return response with json content type" in {
      val response: HttpResponse = getResponseFromActor(TestActorRef[RequestProcessingActor], "123")
      val headers: Seq[HttpHeader] = response.headers
      (headers.size must_== 1) and (headers(0) mustEqual `Content-Type`(`application/json`))
    }

    "handle not akka http requests" in {
      val response: HttpResponse = getResponseFromActor(TestActorRef[RequestProcessingActor], "hello world")
      (response !== null) and (response.status must_==  StatusCodes.NotFound)

    }

    "handle /oauth/authorize GET requests" in {
      val request: HttpRequest = HttpRequest(HttpMethods.GET, uri = "/oauth/authorize")
      val response: HttpResponse = getResponseFromActor(TestActorRef[RequestProcessingActor], request)
      response.status must_== StatusCodes.InternalServerError
    }

    class RequesrProcessingActorForTest extends RequestProcessingActor {
      override val clientDetailsService: ActorRef = context.actorOf(Props(new DumbClientDetailsServiceActor))
      override val oauth2ValidateActor: ActorRef = context.actorOf(Props(new SkipOAuth2RequestValidateServiceActor()))
    }

    "handle /oauth/authorize GET requests with clientId" in {
      val request: HttpRequest = HttpRequest(HttpMethods.GET, uri = "/oauth/authorize?client_id=1")
      val response: HttpResponse = getResponseFromActor(TestActorRef(Props(new RequesrProcessingActorForTest())), request)
      response.status must_== StatusCodes.OK
    }

  }
}
