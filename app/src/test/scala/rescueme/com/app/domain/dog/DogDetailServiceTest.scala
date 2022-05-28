package rescueme.com.app.domain.dog

import cats.effect.IO
import org.mockito.Mockito.when
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.{EitherValues, OptionValues}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import rescueme.com.app.domain._
import org.scalacheck.ScalacheckShapeless._
import cats.effect.unsafe.implicits.global

class DogDetailServiceTest
    extends AnyFunSuite
    with ScalaCheckPropertyChecks
    with Matchers
    with MockitoSugar
    with EitherValues
    with OptionValues {

  val repo: DogDetailRepositoryAlgebra[IO] = mock[DogDetailRepositoryAlgebra[IO]]
  val validator: DogValidator[IO]          = mock[DogValidator[IO]]
  val service: DogDetailService[IO]        = DogDetailService.make[IO](repo, validator)

  test("create new details ok") {

    forAll { details: DogDetail =>
      when(repo.getDetails(details.dogId)).thenReturn(IO.pure(None))
      when(repo.createDetails(details)).thenReturn(IO.pure(details))

      val created = service.create(details).value.unsafeRunSync()

      created shouldBe Right(details)
    }
  }

  test("return details exists when creating") {
    forAll { details: DogDetail =>
      when(repo.getDetails(details.dogId)).thenReturn(IO.pure(Some(details)))

      val created = service.create(details).value.unsafeRunSync()

      created shouldBe Left(DogDetailsAlreadyExists)
    }

  }

}
