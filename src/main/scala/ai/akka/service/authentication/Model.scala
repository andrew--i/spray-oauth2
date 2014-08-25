package ai.akka.service.authentication

/**
 * Created by Andrew on 24.08.2014.
 */
object Model {

  /**
   * Authentication information
   * @param name user name
   * @param isAuthenticated is user authenticated
   */
  case class Authentication(name: String, isAuthenticated: Boolean)

}
