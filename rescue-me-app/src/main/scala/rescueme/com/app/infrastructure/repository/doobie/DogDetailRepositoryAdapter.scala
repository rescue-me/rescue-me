package rescueme.com.app.infrastructure.repository.doobie

import cats.data.OptionT
import cats.effect.Async
import doobie.implicits._
import doobie.util.transactor.Transactor
import rescueme.com.app.domain.Identifier
import rescueme.com.app.domain.dog.{DogDetail, DogDetailRepositoryAlgebra}

object Q {
  def get(id: Identifier): doobie.Query0[DogDetail] =
    sql"""
      SELECT * FROM dog_details WHERE id = $id
       """.query[DogDetail]

  def update(dogDetail: DogDetail): doobie.Update0 = sql"""""".update
  def create(dogDetail: DogDetail): doobie.Update0 =
    sql"""
      INSERT INTO dog_details (id, name, breed, description, gender, size, color, date_of_birth, since)
      VALUES (
        ${dogDetail.dogId},
        ${dogDetail.name},
        ${dogDetail.breed},
        ${dogDetail.description},
        ${dogDetail.gender},
        ${dogDetail.size},
        ${dogDetail.color},
        ${dogDetail.dateOfBirth},
        ${dogDetail.since})""".update
}

object DogDetailRepositoryAdapter {

  def make[F[_]: Async](xa: Transactor[F]): DogDetailRepositoryAlgebra[F] =
    new DogDetailRepositoryAlgebra[F] {
      override def getDetails(id: Identifier): F[Option[DogDetail]] =
        Q.get(id).option.transact(xa)
      override def updateDetails(details: DogDetail): F[Option[DogDetail]] = {
        val trx = for {
          _       <- OptionT(Q.update(details).run.map(affectedToOption))
          updated <- OptionT(Q.get(details.dogId).option)
        } yield updated
        trx.value.transact(xa)
      }
      override def createDetails(detail: DogDetail): F[DogDetail] = {
        val trx = for {
          _       <- Q.create(detail).run
          updated <- Q.get(detail.dogId).unique
        } yield updated
        trx.transact(xa)
      }

    }
}
