package ai.akka.util

import org.specs2.mutable.Specification

/**
 * Created by Andrew on 24.08.2014.
 */
class OAuth2UtilsTest extends Specification {

  "The OAuth2Utils" should {
    "parse parameter list from null string" in {
      OAuth2Utils.parseParameterList(null) must be empty
    }

    "parse parameter list from empty string" in {
      OAuth2Utils.parseParameterList("") must be empty
    }

    "parse parameter list" in {
      OAuth2Utils.parseParameterList("h w") must_== Set("h", "w")
    }
  }

}
