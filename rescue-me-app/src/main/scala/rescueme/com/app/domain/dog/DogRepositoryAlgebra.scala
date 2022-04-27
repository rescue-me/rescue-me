package rescueme.com.app.domain.dog

trait DogRepositoryAlgebra[F[_]] {
  def all(): F[List[Dog]]
  def create(dog: Dog): F[Dog]
  def get(id: Long): F[Option[Dog]]
}
