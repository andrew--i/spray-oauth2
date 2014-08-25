package ai.akka.service.authentication

import ai.akka.service.authentication.Model.Authentication
import akka.http.model.HttpRequest

/**
 * Trait of authentication service
 */
trait AuthenticationService {
  /**
   * The method tries authenticate user by his request
   * @param request user request
   * @return information about authentication
   */
  def authenticateUser(request: HttpRequest): Authentication
}
