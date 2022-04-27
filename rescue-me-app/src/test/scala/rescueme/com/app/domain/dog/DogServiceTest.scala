package rescueme.com.app.domain.dog

import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import org.mockito.Mockito.when
import org.scalatest.{EitherValues, OptionValues}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import rescueme.com.app.domain.DogNotFound

class DogServiceTest extends AnyFlatSpec with Matchers with MockitoSugar with EitherValues with OptionValues{

  behavior of "dog service"

  val dog: Dog                       = Dog("budy-test", "tester", "great testing")
  val repo: DogRepositoryAlgebra[IO] = mock[DogRepositoryAlgebra[IO]]
  val dogService: DogService[IO]     = DogService[IO](repo)

  it should "should create dog" in {

    when(repo.create(dog)).thenReturn(IO(dog))

    val result = dogService.create(dog).value.unsafeRunSync()

    result.value shouldBe dog

  }

  it should "retrieve all dogs" in {
    when(repo.all()).thenReturn(IO(List(dog)))

    val retrieved: List[Dog] = dogService.all().unsafeRunSync()

    retrieved shouldBe List(dog)
  }

  it should "retrieve dog by id" in {
    val id = 1L
    when(repo.get(id)).thenReturn(Some(dog).pure[IO])

    val retrieved = dogService.get(id).value.unsafeRunSync()
    retrieved.value shouldBe dog

  }

  it should "return left with error when dog is not found" in {

    val id = 1L
    when(repo.get(id)).thenReturn(None.pure[IO])

    val retrieved = dogService.get(id).value.unsafeRunSync()
    retrieved shouldBe Left(DogNotFound)

  }

}
