package rescueme.com.app.domain.shelter
import cats.Applicative
import cats.data.EitherT
import rescueme.com.app.domain.{Identifier, ShelterNotFound}

class ShelterValidationInterpreter[F[_]: Applicative](repository: ShelterRepositoryAlgebra[F])
    extends ShelterValidation[F] {
  override def exists(id: Identifier): EitherT[F, ShelterNotFound.type, Shelter] =
    EitherT.fromOptionF(repository.get(id), ShelterNotFound)
}

object ShelterValidationInterpreter {
  def apply[F[_]: Applicative](repositoryAlgebra: ShelterRepositoryAlgebra[F]) =
    new ShelterValidationInterpreter[F](repositoryAlgebra)
}
