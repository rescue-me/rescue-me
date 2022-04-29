package rescueme.com.app.domain.shelter

import cats.effect.IO
import org.mockito.Mockito.when
import org.scalatest.{EitherValues, OptionValues}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import rescueme.com.app.domain.ShelterNotFound

class ShelterServiceTest extends AnyFlatSpec with Matchers with MockitoSugar with EitherValues with OptionValues {

  behavior of "shelter service"

  val shelter: Shelter                                = Shelter("shelter-test", "province-test")
  val repositoryAlgebra: ShelterRepositoryAlgebra[IO] = mock[ShelterRepositoryAlgebra[IO]]
  val shelterService: ShelterService[IO]              = ShelterService[IO](repositoryAlgebra)

  it should "create shelter ok" in {

    when(repositoryAlgebra.create(shelter)).thenReturn(IO.pure(shelter))

    val result = shelterService.create(shelter).value.unsafeRunSync()

    result.value shouldBe shelter
  }

  it should "retrieve all shelters" in {
    when(repositoryAlgebra.all()).thenReturn(IO.pure(List(shelter)))

    shelterService.all().unsafeRunSync() shouldBe List(shelter)
  }

  it should "retrieve shelter by id ok" in {

    val id = 1L
    when(repositoryAlgebra.get(id)).thenReturn(IO.pure(Some(shelter)))

    val result = shelterService.get(id).value.unsafeRunSync()

    result.value shouldBe shelter
  }

  it should "return left when shelter not found" in {

    val id = 1L
    when(repositoryAlgebra.get(id)).thenReturn(IO.pure(None))

    val result = shelterService.get(id).value.unsafeRunSync()

    result shouldBe Left(ShelterNotFound)
  }

}
