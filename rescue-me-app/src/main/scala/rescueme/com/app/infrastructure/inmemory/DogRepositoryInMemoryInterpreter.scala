package rescueme.com.app.infrastructure.inmemory

import cats._
import cats.syntax.all._
import rescueme.com.app.domain.dog.{Dog, DogRepositoryAlgebra}

import java.util.UUID
import scala.collection.concurrent.TrieMap

class DogRepositoryInMemoryInterpreter[F[_]: Monad] extends DogRepositoryAlgebra[F] {

  private val cache = new TrieMap[UUID, Dog]

  override def all(): F[List[Dog]] = {
    cache.values.toList.pure[F]
  }

  override def create(dog: Dog): F[Dog] = {
    val toSave = dog.copy(id = dog.id.orElse(UUID.randomUUID().some))
    cache.put(toSave.id.get, toSave)
    dog.pure[F]
  }
}

object DogRepositoryInMemoryInterpreter {
  def apply[F[_]: Monad] = new DogRepositoryInMemoryInterpreter[F]
}
