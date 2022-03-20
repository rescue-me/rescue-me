package rescueme.com.app.domain.dog

trait DogRepositoryAlgebra[F[_]] {
  def all(): F[List[Dog]]
}
