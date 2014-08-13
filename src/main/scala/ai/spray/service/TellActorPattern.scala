package ai.spray.service

import ai.spray.service.TellActorPattern.WithActorRef
import akka.actor.SupervisorStrategy.Stop
import akka.actor.{Actor, ActorRef, OneForOneStrategy, Props}
import org.json4s.DefaultFormats
import spray.http.StatusCode
import spray.http.StatusCodes._
import spray.httpx.Json4sSupport
import spray.routing.RequestContext

import scala.concurrent.duration._

/**
 * Created by Андрей Смирнов on 11.08.2014.
 */
trait TellActorPattern extends Actor with Json4sSupport {

  import context._

  val json4sFormats = DefaultFormats

  def requestContext: RequestContext

  def target: ActorRef

  def message: Any

  setReceiveTimeout(2 seconds)
  target ! message

  override def receive: Receive = {
    case x: AnyRef => complete(OK, x)
  }

  def complete[T <: AnyRef](status: StatusCode, obj: T) = {
    requestContext.complete(status, obj)
    stop(self)
  }

  override val supervisorStrategy =
    OneForOneStrategy() {
      case e =>
        complete(InternalServerError, e.getMessage)
        Stop
    }
}

object TellActorPattern {

  case class WithActorRef(requestContext: RequestContext, props: Props, message: Any) extends TellActorPattern {
    lazy val target = context.actorOf(props)
  }

}

trait PerRequestCreator {
  this: Actor =>
  def perRequest(r: RequestContext, props: Props, message: Any) =
    context.actorOf(Props(new WithActorRef(r, props, message)))
}
