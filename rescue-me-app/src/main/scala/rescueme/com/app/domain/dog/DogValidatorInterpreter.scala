package rescueme.com.app.domain.dog

import cats.Applicative
import cats.data.EitherT
import rescueme.com.app.domain.{DogNotFound, Identifier}

trait DogValidatorInterpreter[F[_]] {
  def exists(id: Identifier): EitherT[F, DogNotFound.type, Dog]
}

object DogValidatorInterpreter {
  def make[F[_]: Applicative](dogRepositoryAlgebra: DogRepositoryAlgebra[F]): DogValidatorInterpreter[F] =
    (id: Identifier) => EitherT.fromOptionF(dogRepositoryAlgebra.get(id), DogNotFound)
}
