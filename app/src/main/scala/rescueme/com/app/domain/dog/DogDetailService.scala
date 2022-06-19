package rescueme.com.app.domain.dog

import cats.MonadThrow
import cats.data.EitherT
import cats.implicits._
import rescueme.com.app.domain.DomainError._
import rescueme.com.app.domain.Identifier

trait DogDetailService[F[_]] {
  def get(id: Identifier): F[Either[DogNotFound, DogDetail]]
  def update(dogDetail: DogDetail): F[DogDetail]
  def create(dogDetail: DogDetail): F[Either[DogAlreadyExists, DogDetail]]
}

object DogDetailService {
  def make[F[_]: MonadThrow](
      dogRepository: DogDetailRepositoryAlgebra[F]
  ): DogDetailService[F] =
    new DogDetailService[F] {

      override def get(id: Identifier): F[Either[DogNotFound, DogDetail]] =
        EitherT.fromOptionF(dogRepository.getDetails(id), DogNotFound).value

      override def update(dogDetail: DogDetail): F[DogDetail] =
        dogRepository.updateDetails(dogDetail).flatMap {
          case None          => DogDetailsNotUpdated.raiseError[F, DogDetail]
          case Some(details) => details.pure[F]
        }

      override def create(dogDetail: DogDetail): F[Either[DogAlreadyExists, DogDetail]] =
        dogRepository.getDetails(dogDetail.dogId).flatMap {
          case Some(_) => DogAlreadyExists.asLeft[DogDetail].pure[F]
          case None    => dogRepository.createDetails(dogDetail).map(Right(_))
        }

    }
}
