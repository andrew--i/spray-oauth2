package ai.akka.util

/**
 * some helpers for oauth2 methods
 */
object OAuth2Utils {
  /**
   * Parse input string as items with spaces as separators
   * @param parameters input parameters as string
   * @return set of parameters
   */
  def parseParameterList(parameters: String): Set[String] = {
    val result: Array[String] = if (parameters == null || parameters.trim.length == 0)
      Array.empty[String]
    else
      parameters.split("[\\s+]")
    result.toSet
  }

}
