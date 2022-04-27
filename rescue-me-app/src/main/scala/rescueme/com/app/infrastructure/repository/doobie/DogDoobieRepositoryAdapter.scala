package rescueme.com.app.infrastructure.repository.doobie

import cats.effect.Async
import doobie._
import doobie.implicits._
import rescueme.com.app.domain.dog.{Dog, DogRepositoryAlgebra}

object DogSql {

  def insert(dog: Dog): Update0 =
    sql"INSERT INTO dog (name, breed, description) VALUES (${dog.name}, ${dog.breed}, ${dog.description})".update

  def getAll: Query0[Dog] = sql"SELECT * FROM dog".query[Dog]

  def get(id: Long): Query0[Dog] = sql"SELECT * FROM dog where id=$id".query[Dog]
}

class DogDoobieRepositoryAdapter[F[_]: Async](val xa: Transactor[F]) extends DogRepositoryAlgebra[F] {

  import DogSql._
  override def all(): F[List[Dog]] = getAll.stream.compile.toList.transact(xa)

  override def create(dog: Dog): F[Dog] =
    insert(dog)
      .withUniqueGeneratedKeys[Long]("id")
      .map(id => dog.copy(id = Some(id)))
      .transact(xa)

  override def get(id: Long): F[Option[Dog]] = DogSql.get(id).option.transact(xa)
}

object DogDoobieRepositoryAdapter {
  def apply[F[_]: Async](xa: Transactor[F]): DogDoobieRepositoryAdapter[F] =
    new DogDoobieRepositoryAdapter[F](xa)
}
