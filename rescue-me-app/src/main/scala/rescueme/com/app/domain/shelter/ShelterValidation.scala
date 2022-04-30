package rescueme.com.app.domain.shelter

import cats.data.EitherT
import rescueme.com.app.domain.{Identifier, ShelterNotFound}

trait ShelterValidation[F[_]] {
  def exists(id: Identifier): EitherT[F, ShelterNotFound.type, Shelter]
}
