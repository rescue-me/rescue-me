package rescueme.com.app.domain.dog

import rescueme.com.app.domain.Identifier

trait DogDetailRepositoryAlgebra[F[_]] {
  def getDetails(id: Identifier): F[Option[DogDetail]]
  def updateDetails(details: DogDetail): F[Option[DogDetail]]
  def createDetails(detail: DogDetail): F[DogDetail]
}
