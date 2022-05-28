package rescueme.com.app.domain.dog

import cats.Monad
import cats.data.EitherT
import rescueme.com.app.domain.shelter.ShelterValidator
import rescueme.com.app.domain.{DogNotFound, DomainError, Identifier}

trait DogService[F[_]] {
  def all: F[List[Dog]]
  def create(dog: Dog): EitherT[F, DomainError, Dog]
  def get(id: Identifier): EitherT[F, DogNotFound.type, Dog]
  def getByShelter(shelterId: Identifier): F[List[Dog]]
}

object DogService {
  def impl[F[_]: Monad](repository: DogRepositoryAlgebra[F], shelterValidation: ShelterValidator[F]): DogService[F] =
    new DogService[F] {
      def all: F[List[Dog]] = repository.all()

      def create(dog: Dog): EitherT[F, DomainError, Dog] =
        for {
          _       <- shelterValidation.exists(dog.shelterId)
          created <- EitherT.liftF(repository.create(dog))
        } yield created

      def get(id: Identifier): EitherT[F, DogNotFound.type, Dog] = EitherT.fromOptionF(repository.get(id), DogNotFound)

      def getByShelter(shelterId: Identifier): F[List[Dog]] = repository.getByShelter(shelterId)

    }
}
