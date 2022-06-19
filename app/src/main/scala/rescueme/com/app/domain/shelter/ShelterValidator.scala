package rescueme.com.app.domain.shelter

import cats.Applicative
import cats.data.EitherT
import rescueme.com.app.domain.DomainError._
import rescueme.com.app.domain.Identifier

trait ShelterValidator[F[_]] {
  def exists(id: Identifier): EitherT[F, ShelterNotFound.type, Shelter]
}

object ShelterValidator {
  def make[F[_]: Applicative](repositoryAlgebra: ShelterRepositoryAlgebra[F]): ShelterValidator[F] =
    (id: Identifier) => EitherT.fromOptionF(repositoryAlgebra.get(id), ShelterNotFound)
}
