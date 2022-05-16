package rescueme.com.app.infrastructure.repository

import cats.effect.IO
import rescueme.com.app.domain.Identifier
import rescueme.com.app.domain.dog.{DogDetail, DogDetailRepositoryAlgebra}

import java.util.UUID

object DogDetailStubRepository extends DogDetailRepositoryAlgebra[IO] {
  val dogDetail =
    DogDetail(UUID.randomUUID(), "name-test", "bulldog", "brown", "great dog good temper", "Male", "Small")
  override def getDetails(id: Identifier): IO[Option[DogDetail]]        = IO.pure(None)
  override def updateDetails(details: DogDetail): IO[Option[DogDetail]] = IO.pure(None)
  override def createDetails(detail: DogDetail): IO[DogDetail]          = IO.pure(dogDetail)
}
