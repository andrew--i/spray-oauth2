package ai.spray.service

import akka.actor.Actor

/**
 * Created by Andrew on 10.08.2014.
 * Обработчик endpoint-авторизации
 */
class AuthorizeServiceActor extends Actor {
  override def receive: Receive = {
    case params:Map[String, String] => context.parent ! params
  }
}
