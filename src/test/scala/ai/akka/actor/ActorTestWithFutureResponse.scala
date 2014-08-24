package ai.akka.actor

import akka.actor.ActorRef
import akka.util.Timeout
import org.specs2.mutable.Specification
import org.specs2.time.NoTimeConversions
import spray.testkit.Specs2RouteTest
import akka.pattern.ask
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
 * Created by andrew on 23.08.14.
 */
abstract class ActorTestWithFutureResponse extends Specification with Specs2RouteTest with NoTimeConversions {

  implicit val timeout: Timeout = Timeout(1000.millis)

  def getResponseFromActor[T](actorRef:ActorRef, message: Any): T = {
    val future: Future[T] = (actorRef ? message).map(i => i.asInstanceOf[T])
    Await.result[T](future, Duration("1000ms"))
  }

}
