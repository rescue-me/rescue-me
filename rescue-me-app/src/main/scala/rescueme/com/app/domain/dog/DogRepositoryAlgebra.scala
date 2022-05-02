package rescueme.com.app.domain.dog

import rescueme.com.app.domain.Identifier

trait DogRepositoryAlgebra[F[_]] {
  def all(): F[List[Dog]]
  def create(dog: Dog): F[Dog]
  def get(id: Identifier): F[Option[Dog]]
}
