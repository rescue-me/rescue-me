package rescueme.com.app.infrastructure.repository

import cats.effect.IO
import rescueme.com.app.domain.shelter.{Shelter, ShelterRepositoryAlgebra}

import scala.util.Random

object ShelterStubRepository extends ShelterRepositoryAlgebra[IO] {
  private val shelter                             = Shelter(Some(Random.nextLong), "test-name", "test-description")
  private val shelterList                         = List(shelter)
  override def all(): IO[List[Shelter]]           = IO.pure(shelterList)
  override def create(req: Shelter): IO[Shelter]  = IO.pure(shelter)
  override def get(id: Long): IO[Option[Shelter]] = IO.pure(Some(shelter))
}
