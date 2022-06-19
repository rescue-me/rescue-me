package rescueme.com.app.domain.shelter

import cats.effect.IO
import org.mockito.Mockito.when
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{EitherValues, OptionValues}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import rescueme.com.app.domain.DomainError._
import org.scalacheck.ScalacheckShapeless._
import org.scalatest.funsuite.AnyFunSuite
import cats.effect.unsafe.implicits.global

import java.util.UUID

class ShelterServiceTest
  extends AnyFunSuite
    with ScalaCheckPropertyChecks
    with Matchers
    with MockitoSugar
    with EitherValues
    with OptionValues {

  val repositoryAlgebra: ShelterRepositoryAlgebra[IO] = mock[ShelterRepositoryAlgebra[IO]]
  val shelterService: ShelterService[IO]              = ShelterService.make[IO](repositoryAlgebra)

  test("create shelter ok"){

    forAll { shelter: Shelter =>
      when(repositoryAlgebra.create(shelter)).thenReturn(IO.pure(shelter))

      val result = shelterService.create(shelter).value.unsafeRunSync()

      result.value shouldBe shelter
    }
  }

  test("retrieve all shelters" ) {

    forAll { shelter: Shelter =>
      when(repositoryAlgebra.all()).thenReturn(IO.pure(List(shelter)))

      shelterService.all.unsafeRunSync() shouldBe List(shelter)
    }
  }

  test( "retrieve shelter by id ok" ) {

    forAll { shelter: Shelter =>
      val id = UUID.randomUUID()
      when(repositoryAlgebra.get(id)).thenReturn(IO.pure(Some(shelter)))

      val result = shelterService.get(id).value.unsafeRunSync()

      result.value shouldBe shelter
    }
  }

  test( "return left when shelter not found" ) {
    forAll { shelter: Shelter =>
      val id = UUID.randomUUID()
      when(repositoryAlgebra.get(id)).thenReturn(IO.pure(None))

      val result = shelterService.get(id).value.unsafeRunSync()

      result shouldBe Left(ShelterNotFound)
    }
  }

}
