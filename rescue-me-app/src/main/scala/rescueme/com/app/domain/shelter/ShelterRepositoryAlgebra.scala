package rescueme.com.app.domain.shelter

trait ShelterRepositoryAlgebra[F[_]] {
  def all(): F[List[Shelter]]
  def create(shelter: Shelter): F[Shelter]
  def get(id: Long): F[Option[Shelter]]
}
