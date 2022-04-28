package rescueme.com.app.infrastructure.repository.doobie

import cats.effect.Async
import doobie.implicits._
import doobie.{Query0, Transactor, Update0}
import rescueme.com.app.domain.shelter.{Shelter, ShelterRepositoryAlgebra}

object ShelterSql {

  def insert(shelter: Shelter): Update0 =
    sql"INSERT INTO shelter (name, province) VALUES (${shelter.name},${shelter.province})".update

  def getAll: Query0[Shelter] = sql"SELECT * FROM shelter".query[Shelter]

  def get(id: Long): Query0[Shelter] = sql"SELECT * FROM shelter where id=$id".query[Shelter]

}
class ShelterDoobieRepositoryAdapter[F[_]](val xa: Transactor[F]) extends ShelterRepositoryAlgebra[F] {

  override def all(): F[List[Shelter]] = ???

  override def create(shelter: Shelter): F[Shelter] = ???

  override def get(id: Long): F[Option[Shelter]] = ???
}
object ShelterDoobieRepositoryAdapter {
  def apply[F[_]: Async](xa: Transactor[F]) = new ShelterDoobieRepositoryAdapter[F](xa)
}
