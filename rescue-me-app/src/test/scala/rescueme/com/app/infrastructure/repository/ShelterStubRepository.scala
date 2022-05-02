package rescueme.com.app.infrastructure.repository

import cats.effect.IO
import rescueme.com.app.domain.shelter.{Shelter, ShelterRepositoryAlgebra}

import java.util.UUID

object ShelterStubRepository extends ShelterRepositoryAlgebra[IO] {
  private val shelter                             = Shelter(Some(UUID.randomUUID()), "test-name", "test-description")
  private val shelterList                         = List(shelter)
  override def all(): IO[List[Shelter]]           = IO.pure(shelterList)
  override def create(req: Shelter): IO[Shelter]  = IO.pure(shelter)
  override def get(id: UUID): IO[Option[Shelter]] = IO.pure(Some(shelter))
}
