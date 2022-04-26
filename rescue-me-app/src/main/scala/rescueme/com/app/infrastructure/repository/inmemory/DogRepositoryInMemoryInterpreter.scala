package rescueme.com.app.infrastructure.repository.inmemory

import cats._
import cats.syntax.all._
import rescueme.com.app.domain.dog.{Dog, DogRepositoryAlgebra}

import java.util.UUID
import scala.collection.concurrent.TrieMap
import scala.util.Random

class DogRepositoryInMemoryInterpreter[F[_]: Monad] extends DogRepositoryAlgebra[F] {

  private val cache = new TrieMap[Long, Dog]

  override def all(): F[List[Dog]] = {
    cache.values.toList.pure[F]
  }

  override def create(dog: Dog): F[Dog] = {
    val toSave = dog.copy(id = dog.id.orElse(Random.nextLong().some))
    cache.put(toSave.id.get, toSave)
    toSave.pure[F]
  }
}

object DogRepositoryInMemoryInterpreter {
  def apply[F[_]: Monad] = new DogRepositoryInMemoryInterpreter[F]
}
