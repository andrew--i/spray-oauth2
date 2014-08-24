package ai.akka.service.authentication

import ai.akka.service.authentication.Model.Authentication
import akka.http.model.HttpRequest

/**
 * Created by Andrew on 24.08.2014.
 */
trait AuthenticationService {
  def authenticateUser(request: HttpRequest): Authentication
}
