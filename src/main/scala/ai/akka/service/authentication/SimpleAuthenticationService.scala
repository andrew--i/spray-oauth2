package ai.akka.service.authentication

import ai.akka.service.authentication.Model.Authentication
import akka.http.model.HttpRequest

/**
 * Trait of simple authentication,
 * All users are anonymous
 */
trait SimpleAuthenticationService extends AuthenticationService {
  val anonymous: Authentication = Authentication("anonymous", isAuthenticated = true)

  override def authenticateUser(request: HttpRequest): Authentication = anonymous
}
