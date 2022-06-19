package rescueme.com.app.domain.dog

import cats.effect.IO
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.{EitherValues, OptionValues}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class DogDetailServiceTest
    extends AnyFunSuite
    with ScalaCheckPropertyChecks
    with Matchers
    with MockitoSugar
    with EitherValues
    with OptionValues {

  val repo: DogDetailRepositoryAlgebra[IO] = mock[DogDetailRepositoryAlgebra[IO]]
  val service: DogDetailService[IO]        = DogDetailService.make[IO](repo)

}
