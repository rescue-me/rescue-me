package rescueme.com.app.infrastructure.inmemory

import cats._
import cats.syntax.all._
import rescueme.com.app.domain.dog.{Dog, DogRepositoryAlgebra}

class DogRepositoryInMemoryInterpreter[F[_]: Monad] extends DogRepositoryAlgebra[F] {
  val dogs = List(
    Dog("Budy", "Frenchie", "Dark color"),
    Dog("Kira", "Labrador", "White color, puppy"),
    Dog("Kitty", "Bichon", "Withe color")
  )
  override def all(): F[List[Dog]] = dogs.pure[F]
}

object DogRepositoryInMemoryInterpreter {
  def apply[F[_]: Monad] = new DogRepositoryInMemoryInterpreter[F]
}
