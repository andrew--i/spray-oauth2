package ai.spray.oauth2.actor.message

import spray.routing.RequestContext

/**
 * Created by Andrew on 10.08.2014.
 */
object RequestMessage {

  trait BaseRequest {
    val ctx: RequestContext
  }

  case class AuthorizationGetRequestMessage(ctx: RequestContext) extends BaseRequest

  case class AuthorizationPostRequestMessage(ctx: RequestContext) extends BaseRequest

}
