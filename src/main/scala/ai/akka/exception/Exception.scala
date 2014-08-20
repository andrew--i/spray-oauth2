package ai.akka.exception

/**
 * Created by Андрей Смирнов on 20.08.2014.
 */
object Exception {

  case class OAuthParseRequestException(msg: String) extends Throwable {
    override def getMessage: String = msg
  }

}
