package rescueme.com.rescue_me_backoffice

import org.scalatest._
import org.scalatest.Matchers._

final class RescueMeBackofficeTest extends WordSpec with GivenWhenThen {
  "RescueMeBackoffice" should {
    "greet" in {
      Given("a RescueMeBackoffice")

      val rescueMeBackoffice = new RescueMeBackoffice

      When("we ask him to greet someone")

      val nameToGreet = "CodelyTV"
      val greeting = rescueMeBackoffice.greet(nameToGreet)

      Then("it should say hello to someone")

      greeting shouldBe "Hello " + nameToGreet
    }
  }
}
