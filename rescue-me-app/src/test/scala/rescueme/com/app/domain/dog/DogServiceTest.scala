package rescueme.com.app.domain.dog

import cats.effect.IO
import org.mockito.Mockito.when
import org.scalatest.EitherValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar

class DogServiceTest extends AnyFlatSpec with Matchers with MockitoSugar with EitherValues {

  behavior of "dog service"

  val dog: Dog                       = Dog("budy-test", "tester", "great testing")
  val repo: DogRepositoryAlgebra[IO] = mock[DogRepositoryAlgebra[IO]]
  val dogService: DogService[IO]     = DogService[IO](repo)

  it should "should create dog" in {

    when(repo.create(dog)).thenReturn(IO(dog))

    val result = dogService.create(dog).value.unsafeRunSync()

    result.value shouldBe dog

  }

  it should "retrieve all dogs" in {}

  when(repo.all()).thenReturn(IO(List(dog)))

  val retrieved: List[Dog] = dogService.all().unsafeRunSync()

  retrieved shouldBe List(dog)

}
