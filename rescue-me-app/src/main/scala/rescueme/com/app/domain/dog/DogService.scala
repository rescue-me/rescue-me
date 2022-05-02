package rescueme.com.app.domain.dog

import cats.Monad
import cats.data.EitherT
import rescueme.com.app.domain.{DogNotFound, DomainError, Identifier}
import rescueme.com.app.domain.shelter.ShelterValidation

class DogService[F[_]: Monad](repository: DogRepositoryAlgebra[F], shelterValidation: ShelterValidation[F]) {

  def all(): F[List[Dog]] = repository.all()

  def create(dog: Dog): EitherT[F, DomainError, Dog] =
    for {
      _       <- shelterValidation.exists(dog.shelterId)
      created <- EitherT.liftF(repository.create(dog))
    } yield created

  def get(id: Identifier): EitherT[F, DogNotFound.type, Dog] = EitherT.fromOptionF(repository.get(id), DogNotFound)

  def getByShelter(shelterId: Identifier): F[List[Dog]] = repository.getByShelter(shelterId)

}

object DogService {
  def apply[F[_]: Monad](repositoryAlgebra: DogRepositoryAlgebra[F],
                         shelterValidation: ShelterValidation[F]): DogService[F] =
    new DogService[F](repositoryAlgebra, shelterValidation)
}
