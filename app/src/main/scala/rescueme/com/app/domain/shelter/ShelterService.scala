package rescueme.com.app.domain.shelter

import cats.Functor
import rescueme.com.app.domain.Identifier

trait ShelterService[F[_]] {
  def all: F[List[Shelter]]
  def create(shelter: Shelter): F[Shelter]
  def get(id: Identifier): F[Option[Shelter]]
}

object ShelterService {
  def make[F[_]: Functor](repository: ShelterRepositoryAlgebra[F]): ShelterService[F] =
    new ShelterService[F] {

      def all: F[List[Shelter]]                   = repository.all()
      def create(shelter: Shelter): F[Shelter]    = repository.create(shelter)
      def get(id: Identifier): F[Option[Shelter]] = repository.get(id)
    }
}
