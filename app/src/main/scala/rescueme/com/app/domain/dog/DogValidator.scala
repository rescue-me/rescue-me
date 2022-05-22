package rescueme.com.app.domain.dog

import cats.Applicative
import cats.data.EitherT
import rescueme.com.app.domain.{DogNotFound, Identifier}

trait DogValidator[F[_]] {
  def exists(id: Identifier): EitherT[F, DogNotFound.type, Dog]
}

object DogValidator {
  def make[F[_]: Applicative](dogRepositoryAlgebra: DogRepositoryAlgebra[F]): DogValidator[F] =
    (id: Identifier) => EitherT.fromOptionF(dogRepositoryAlgebra.get(id), DogNotFound)
}