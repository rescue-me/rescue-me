package rescueme.com.app.domain.shelter

import cats.Functor
import cats.data.EitherT
import rescueme.com.app.domain.{Identifier, ShelterNotFound}

trait ShelterService[F[_]] {
  def all: F[List[Shelter]]
  def create(shelter: Shelter): EitherT[F, Throwable, Shelter]
  def get(id: Identifier): EitherT[F, ShelterNotFound.type, Shelter]
}

object ShelterService {
  def make[F[_]: Functor](repository: ShelterRepositoryAlgebra[F]): ShelterService[F] = new ShelterService[F] {
    def all: F[List[Shelter]]                                  = repository.all()
    def create(shelter: Shelter): EitherT[F, Throwable, Shelter] = EitherT.liftF(repository.create(shelter))
    def get(id: Identifier): EitherT[F, ShelterNotFound.type, Shelter] =
      EitherT.fromOptionF(repository.get(id), ShelterNotFound)
  }
}
