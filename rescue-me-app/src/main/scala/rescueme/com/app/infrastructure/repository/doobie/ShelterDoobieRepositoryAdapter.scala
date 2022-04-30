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

class ShelterDoobieRepositoryAdapter[F[_]: Async](val xa: Transactor[F]) extends ShelterRepositoryAlgebra[F] {

  import ShelterSql._

  override def all(): F[List[Shelter]]           = getAll.stream.compile.toList.transact(xa)
  override def get(id: Long): F[Option[Shelter]] = ShelterSql.get(id).option.transact(xa)
  override def create(shelter: Shelter): F[Shelter] =
    insert(shelter)
      .withUniqueGeneratedKeys[Long]("id")
      .map(id => shelter.copy(id = Some(id)))
      .transact(xa)
}
object ShelterDoobieRepositoryAdapter {
  def apply[F[_]: Async](xa: Transactor[F]) = new ShelterDoobieRepositoryAdapter[F](xa)
}
