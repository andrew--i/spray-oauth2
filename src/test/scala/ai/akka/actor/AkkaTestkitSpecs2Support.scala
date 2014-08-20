package ai.akka.actor

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.specs2.mutable.After

/**
 * Created by Андрей Смирнов on 20.08.2014.
 */
abstract class AkkaTestkitSpecs2Support extends TestKit(ActorSystem()) with After with ImplicitSender {
  def after = system.shutdown()
}