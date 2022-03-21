package rescueme.com.app.infrastructure.inmemory

import org.scalatest.OptionValues
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import rescueme.com.app.Arbitraries
import rescueme.com.app.domain.dog.Dog

class DogRepositoryInMemoryInterpreterSpec
    extends AnyFunSuite
    with Matchers
    with OptionValues
    with Arbitraries
    with ScalaCheckPropertyChecks {

  import cats.instances.option._
  val repository = new DogRepositoryInMemoryInterpreter[Option]

  test("Should create new dog and retrieve dog") {
    forAll { (dog: Dog) =>
      repository.create(dog).value shouldEqual dog
      repository.all().value.size should be > 0
    }
  }

}
