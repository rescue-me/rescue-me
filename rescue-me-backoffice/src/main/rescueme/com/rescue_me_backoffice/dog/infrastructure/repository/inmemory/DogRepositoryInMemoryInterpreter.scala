package rescueme.com.rescue_me_backoffice.dog.infrastructure.repository.inmemory

import rescueme.com.rescue_me_backoffice.dog.domain.{Dog, DogRepositoryAlgebra}

import scala.collection.concurrent.TrieMap
import cats._
import cats.implicits._

class DogRepositoryInMemoryInterpreter[F[_]: Applicative] extends DogRepositoryAlgebra[F] {
  private val cache = new TrieMap[Long, Dog]

  override def list(): F[List[Dog]] = cache.values.toList.sortBy(_.name).pure[F]
}
