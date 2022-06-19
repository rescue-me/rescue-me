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
      dogRepository: DogDetailRepositoryAlgebra[F],
      dogValidatorInterpreter: DogValidator[F]
  ): DogDetailService[F] =
    new DogDetailService[F] {

      override def get(id: Identifier): F[Either[DogNotFound, DogDetail]] =
        EitherT.fromOptionF(dogRepository.getDetails(id), DogNotFound).value

      override def update(dogDetail: DogDetail): F[DogDetail] =
        for {
          _ <- dogValidatorInterpreter
            .exists(dogDetail.dogId)
            .flatMap { case Left(_) =>
              DogDetailsNotUpdated.raiseError
            }

          result <- dogRepository.updateDetails(dogDetail).flatMap {
            case None          => DogDetailsNotUpdated.raiseError
            case Some(details) => details.pure[F]
          }

        } yield result

      override def create(dogDetail: DogDetail): F[Either[DogAlreadyExists, DogDetail]] =
        for {
          _ <-
            dogRepository.getDetails(dogDetail.dogId) flatMap {
              case Some(_) =>
                Either.left[DogDetailsAlreadyExists.type, Unit](DogDetailsAlreadyExists).pure[F]
              case None => Either.right[DogDetailsAlreadyExists.type, Unit](()).pure[F]
            }
          created <- dogRepository.createDetails(dogDetail)
        } yield created
    }
}
