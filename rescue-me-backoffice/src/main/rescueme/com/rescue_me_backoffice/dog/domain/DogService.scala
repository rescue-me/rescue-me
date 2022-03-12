package rescueme.com.rescue_me_backoffice.dog.domain

trait DogService[F[_]] {

  def register(dog: Dog): Unit

  def all(): F[Seq[Dog]]
}
