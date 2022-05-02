package rescueme.com.app.domain.dog

import cats.data.EitherT
import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import org.mockito.Mockito.when
import org.scalatest.{EitherValues, OptionValues}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import rescueme.com.app.domain.{DogNotFound, ShelterNotFound}
import rescueme.com.app.domain.shelter.{Shelter, ShelterValidation}

import java.util.UUID
import scala.util.Random

class DogServiceTest extends AnyFlatSpec with Matchers with MockitoSugar with EitherValues with OptionValues {

  behavior of "dog service"
  val shelter: Shelter                         = Shelter(Some(UUID.randomUUID()), "test-name", "test-description")
  val dog: Dog                                 = Dog("budy-test", "tester", "great testing", UUID.randomUUID())
  val repo: DogRepositoryAlgebra[IO]           = mock[DogRepositoryAlgebra[IO]]
  val shelterValidation: ShelterValidation[IO] = mock[ShelterValidation[IO]]
  val dogService: DogService[IO]               = DogService[IO](repo, shelterValidation)

  it should "should create dog" in {

    when(repo.create(dog)).thenReturn(IO.pure(dog))
    when(shelterValidation.exists(dog.shelterId)).thenReturn(EitherT.pure(shelter))

    val result = dogService.create(dog).value.unsafeRunSync()

    result.value shouldBe dog

  }

  it should "should return left when creating dog with not found shelter" in {

    when(shelterValidation.exists(dog.shelterId)).thenReturn(EitherT.leftT(ShelterNotFound))

    val result = dogService.create(dog).value.unsafeRunSync()

    result shouldBe Left(ShelterNotFound)

  }

  it should "retrieve all dogs" in {
    when(repo.all()).thenReturn(IO(List(dog)))

    val retrieved: List[Dog] = dogService.all().unsafeRunSync()

    retrieved shouldBe List(dog)
  }

  it should "retrieve dog by id" in {
    val id = UUID.randomUUID()
    when(repo.get(id)).thenReturn(Some(dog).pure[IO])

    val retrieved = dogService.get(id).value.unsafeRunSync()
    retrieved.value shouldBe dog

  }

  it should "return left with error when dog is not found" in {

    val id = UUID.randomUUID()
    when(repo.get(id)).thenReturn(None.pure[IO])

    val retrieved = dogService.get(id).value.unsafeRunSync()
    retrieved shouldBe Left(DogNotFound)

  }

}
