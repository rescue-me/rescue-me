package rescueme.com.app.infrastructure.repository.doobie

import cats.effect.Async
import doobie._
import doobie.implicits._
import doobie.postgres.implicits._
import rescueme.com.app.domain.Identifier
import rescueme.com.app.domain.dog.{Dog, DogRepositoryAlgebra}

import java.util.UUID

object DogSql {

  def insert(dog: Dog): Update0 =
    sql"""
         INSERT INTO dog (id, name, breed, description,shelter_id)
         VALUES (
                 ${UUID.randomUUID()},
                 ${dog.name}, 
                 ${dog.breed}, 
                 ${dog.description},
                 ${dog.shelterId})""".update

  def getAll: Query0[Dog] = sql"SELECT * FROM dog".query[Dog]

  def get(id: Identifier): Query0[Dog] = sql"SELECT * FROM dog where id=$id".query[Dog]

  def getByShelterId(id: Identifier): Query0[Dog] =
    sql"SELECT * FROM dog where shelter_id=$id".query[Dog]
}

class DogDoobieRepositoryAdapter[F[_]: Async](val xa: Transactor[F])
    extends DogRepositoryAlgebra[F] {

  import DogSql._

  override def all(): F[List[Dog]]                 = getAll.stream.compile.toList.transact(xa)
  override def get(id: Identifier): F[Option[Dog]] = DogSql.get(id).option.transact(xa)
  override def create(dog: Dog): F[Dog] =
    insert(dog)
      .withUniqueGeneratedKeys[UUID]("id")
      .map(id => dog.copy(id = Some(id)))
      .transact(xa)

  override def getByShelter(shelterId: Identifier): F[List[Dog]] =
    DogSql.getByShelterId(shelterId).stream.compile.toList.transact(xa)
}

object DogDoobieRepositoryAdapter {
  def apply[F[_]: Async](xa: Transactor[F]): DogDoobieRepositoryAdapter[F] =
    new DogDoobieRepositoryAdapter[F](xa)
}
