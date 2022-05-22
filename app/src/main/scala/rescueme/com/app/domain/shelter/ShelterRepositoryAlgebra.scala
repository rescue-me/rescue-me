package rescueme.com.app.domain.shelter

import rescueme.com.app.domain.Identifier

trait ShelterRepositoryAlgebra[F[_]] {
  def all(): F[List[Shelter]]
  def create(shelter: Shelter): F[Shelter]
  def get(id: Identifier): F[Option[Shelter]]
}
