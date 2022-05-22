package rescueme.com.app.domain.dog

import cats.data.EitherT
import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import org.mockito.Mockito.when
import org.scalacheck.ScalacheckShapeless._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.{EitherValues, OptionValues}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import rescueme.com.app.domain.shelter.{Shelter, ShelterValidator}
import rescueme.com.app.domain.{DogNotFound, ShelterNotFound}

import java.util.UUID

class DogServiceTest
    extends AnyFunSuite
    with ScalaCheckPropertyChecks
    with Matchers
    with MockitoSugar
    with EitherValues
    with OptionValues {

  val shelter: Shelter                        = Shelter(Some(UUID.randomUUID()), "test-name", "test-description")
  val repo: DogRepositoryAlgebra[IO]          = mock[DogRepositoryAlgebra[IO]]
  val shelterValidation: ShelterValidator[IO] = mock[ShelterValidator[IO]]
  val dogService: DogService[IO]              = DogService[IO](repo, shelterValidation)

  test("should create dog") {
    forAll { dog: Dog =>

      when(repo.create(dog)).thenReturn(IO.pure(dog))
      when(shelterValidation.exists(dog.shelterId)).thenReturn(EitherT.pure(shelter))

      val result = dogService.create(dog).value.unsafeRunSync()

      result.value shouldBe dog

    }

  }

  test("should return left when creating dog with not found shelter") {
    forAll { dog: Dog =>
      when(shelterValidation.exists(dog.shelterId)).thenReturn(EitherT.leftT(ShelterNotFound))

      val result = dogService.create(dog).value.unsafeRunSync()

      result shouldBe Left(ShelterNotFound)
    }

  }

  test("retrieve all dogs") {
    forAll { dog: Dog =>
      when(repo.all()).thenReturn(IO(List(dog)))

      val retrieved: List[Dog] = dogService.all().unsafeRunSync()

      retrieved shouldBe List(dog)
    }
  }

  test("retrieve all dogs by shelter") {

    forAll { (dog: Dog, shelterId: UUID) =>
      when(repo.getByShelter(shelterId)).thenReturn(IO(List(dog)))

      val retrieved: List[Dog] = dogService.getByShelter(shelterId).unsafeRunSync()

      retrieved shouldBe List(dog)
    }
  }

  test("retrieve dog by id") {
    forAll { (dog: Dog, shelterId: UUID) =>

      when(repo.get(shelterId)).thenReturn(Some(dog).pure[IO])

      val retrieved = dogService.get(shelterId).value.unsafeRunSync()
      retrieved.value shouldBe dog
    }

  }

  test("return left with error when dog is not found") {
    forAll {( dog: Dog, shelterId: UUID )=>

      when(repo.get(shelterId)).thenReturn(None.pure[IO])

      val retrieved = dogService.get(shelterId).value.unsafeRunSync()
      retrieved shouldBe Left(DogNotFound)
    }


}
