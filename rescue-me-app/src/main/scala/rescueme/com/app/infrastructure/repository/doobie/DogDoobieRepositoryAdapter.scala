package rescueme.com.app.infrastructure.repository.doobie

import cats.effect.Async
import cats.implicits._
import doobie._
import doobie.implicits._
import rescueme.com.app.domain.dog.{Dog, DogRepositoryAlgebra}

import java.util.UUID

object DogSql {

  implicit val uuidMeta: Meta[UUID] = Meta[String].imap(UUID.fromString)( _.toString)

  def insert(dog: Dog): Update0 =
    sql"""
      INSERT INTO dog (NAME, BREED, DESCRIPTION) 
      VALUES (${dog.name}, ${dog.breed}, ${dog.description})
       """.update
}
class DogDoobieRepositoryAdapter[P[_]: Async](val db: DoobieDbConnection[P]) extends DogRepositoryAlgebra[P] {

  import DogSql._
  override def all(): P[List[Dog]] = List[Dog]().pure[P]

  override def create(dog: Dog): P[Dog] =
    insert(dog)
      .withUniqueGeneratedKeys[UUID]("ID")
      .map(id => dog.copy(id = Some(id)))
      .transact(db.transactor)

}

object DogDoobieRepositoryAdapter {
  def apply[F[_]: Async](db: DoobieDbConnection[F]): DogDoobieRepositoryAdapter[F] =
    new DogDoobieRepositoryAdapter[F](db)
}
