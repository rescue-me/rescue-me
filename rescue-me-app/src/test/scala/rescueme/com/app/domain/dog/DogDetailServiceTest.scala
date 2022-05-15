package rescueme.com.app.domain.dog

import cats.effect.IO
import org.mockito.Mockito.when
import org.scalatest.{EitherValues, OptionValues}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar

import java.util.UUID
import rescueme.com.app.domain._
import rescueme.com.app.domain.Size._
import rescueme.com.app.domain.Gender._

class DogDetailServiceTest extends AnyFlatSpec with Matchers with MockitoSugar with EitherValues with OptionValues {

  behavior of "dog detail service"

  val repo: DogDetailRepositoryAlgebra[IO]   = mock[DogDetailRepositoryAlgebra[IO]]
  val validator: DogValidatorInterpreter[IO] = mock[DogValidatorInterpreter[IO]]
  val service: DogDetailService[IO]          = DogDetailService.make[IO](repo, validator)
  val details: DogDetail =
    DogDetail(UUID.randomUUID(), "name-test", "bulldog", "brown", "great dog good temper", Male, Small)

  it should "create new details ok" in {

    when(repo.getDetails(details.dogId)).thenReturn(IO.pure(None))
    when(repo.createDetails(details)).thenReturn(IO.pure(details))

    val created = service.create(details).value.unsafeRunSync()

    created shouldBe Right(details)

  }

  it should "return details exists when creating" in {
    when(repo.getDetails(details.dogId)).thenReturn(IO.pure(Some(details)))

    val created = service.create(details).value.unsafeRunSync()

    created shouldBe Left(DogDetailsAlreadyExists)

  }

}
