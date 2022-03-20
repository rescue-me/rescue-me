package rescueme.com.rescue_me_backoffice.dog.domain

trait DogRepositoryAlgebra[F[_]] {

  def list(): F[List[Dog]]
  def register(dog: Dog): F[Dog]
}
