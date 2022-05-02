package rescueme.com.app.infrastructure.repository

import cats.effect.IO
import rescueme.com.app.domain.Identifier
import rescueme.com.app.domain.dog.{Dog, DogRepositoryAlgebra}

import java.util.UUID

object DogStubRepository extends DogRepositoryAlgebra[IO] {

  private val dog: Dog                              = Dog(Some(UUID.randomUUID()), "dog-test", "breed-test", "black", UUID.randomUUID())
  override def all(): IO[List[Dog]]                 = IO.pure(List(dog))
  override def create(req: Dog): IO[Dog]            = IO.pure(dog)
  override def get(id: Identifier): IO[Option[Dog]] = IO.pure(Some(dog))
}
