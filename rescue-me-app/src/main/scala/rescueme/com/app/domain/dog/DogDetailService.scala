package rescueme.com.app.domain.dog

import cats.Monad
import cats.data.EitherT
import cats.implicits._
import rescueme.com.app.domain.{DogDetailsAlreadyExists, DogDetailsNotUpdated, DogNotFound, Identifier}

trait DogDetailService[F[_]] {
  def get(id: Identifier): EitherT[F, DogNotFound.type, DogDetail]
  def update(dogDetail: DogDetail): EitherT[F, DogDetailsNotUpdated.type, DogDetail]
  def create(dogDetail: DogDetail): EitherT[F, DogDetailsAlreadyExists.type, DogDetail]
}

object DogDetailService {
  def make[F[_]: Monad](dogRepository: DogDetailRepositoryAlgebra[F],
                        dogValidatorInterpreter: DogValidator[F]): DogDetailService[F] =
    new DogDetailService[F] {
      override def get(id: Identifier): EitherT[F, DogNotFound.type, DogDetail] =
        EitherT.fromOptionF(dogRepository.getDetails(id), DogNotFound)

      override def update(dogDetail: DogDetail): EitherT[F, DogDetailsNotUpdated.type, DogDetail] =
        for {
          _      <- dogValidatorInterpreter.exists(dogDetail.dogId).leftMap(_ => DogDetailsNotUpdated)
          result <- EitherT.fromOptionF(dogRepository.updateDetails(dogDetail), DogDetailsNotUpdated)
        } yield result

      override def create(dogDetail: DogDetail): EitherT[F, DogDetailsAlreadyExists.type, DogDetail] =
        for {
          _ <- EitherT {
            dogRepository.getDetails(dogDetail.dogId) flatMap  {
              case Some(_) => Either.left[DogDetailsAlreadyExists.type, Unit](DogDetailsAlreadyExists).pure[F]
              case None    => Either.right[DogDetailsAlreadyExists.type, Unit](()).pure[F]
            }
          }
          created <- EitherT.liftF[F, DogDetailsAlreadyExists.type, DogDetail](dogRepository.createDetails(dogDetail))
        } yield created
    }
}
