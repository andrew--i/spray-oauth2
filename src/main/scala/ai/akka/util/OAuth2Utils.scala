package ai.akka.util

/**
 * Created by Andrew on 20.08.2014.
 */
object OAuth2Utils {
  def parseParameterList(parameters: String): Set[String] = {
    val result: Array[String] = if (parameters == null || parameters.trim.length == 0)
      Array.empty[String]
    else
      parameters.split("[\\s+]")
    result.toSet
  }

}
