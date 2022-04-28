package rescueme.com.app.domain.shelter

import cats.Functor
import cats.data.EitherT
import rescueme.com.app.domain.ShelterNotFound

class ShelterService[F[_]: Functor](repository: ShelterRepositoryAlgebra[F]) {

  def all(): F[List[Shelter]]                                  = repository.all()
  def create(shelter: Shelter): EitherT[F, Throwable, Shelter] = EitherT.liftF(repository.create(shelter))
  def get(id: Long): EitherT[F, ShelterNotFound.type, Shelter] =
    EitherT.fromOptionF(repository.get(id), ShelterNotFound)

}

object ShelterService {
  def apply[F[_]: Functor](repository: ShelterRepositoryAlgebra[F]) = new ShelterService[F](repository)
}
