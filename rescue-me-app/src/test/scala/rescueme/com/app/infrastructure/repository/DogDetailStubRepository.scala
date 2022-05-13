package rescueme.com.app.infrastructure.repository

import cats.effect.IO
import rescueme.com.app.domain.Identifier
import rescueme.com.app.domain.dog.{DogDetail, DogDetailRepositoryAlgebra}

object DogDetailStubRepository extends DogDetailRepositoryAlgebra[IO] {
  override def getDetails(id: Identifier): IO[List[DogDetail]]                          = IO.pure(List())
  override def updateDetails(id: Identifier, details: DogDetail): IO[Option[DogDetail]] = IO.pure(None)
}
