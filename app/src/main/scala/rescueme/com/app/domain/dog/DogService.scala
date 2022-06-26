package rescueme.com.app.domain.dog

import cats.Monad
import cats.implicits._
import rescueme.com.app.domain.DomainError._
import rescueme.com.app.domain.Identifier
import rescueme.com.app.domain.shelter.ShelterValidator

trait DogService[F[_]] {
  def all: F[List[Dog]]
  def create(dog: Dog): F[Either[ShelterNotFound, Dog]]
  def get(id: Identifier): F[Option[Dog]]
  def getByShelter(shelterId: Identifier): F[List[Dog]]
}

object DogService {
  def make[F[_]: Monad](
      repository: DogRepositoryAlgebra[F],
      shelterValidation: ShelterValidator[F]
  ): DogService[F] =
    new DogService[F] {
      def all: F[List[Dog]] = repository.all()

      def create(dog: Dog): F[Either[ShelterNotFound, Dog]] =
        shelterValidation
          .exists(dog.shelterId)
          .flatMap {
            case Left(value) => value.asLeft[Dog].pure[F]
            case Right(_)    => repository.create(dog).map(_.asRight)
          }

      def get(id: Identifier): F[Option[Dog]] =
        repository.get(id)

      def getByShelter(shelterId: Identifier): F[List[Dog]] = repository.getByShelter(shelterId)

    }
}
