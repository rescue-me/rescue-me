package rescueme.com.app.infrastructure

import cats.data.{Validated, ValidatedNec}
import cats.effect.Sync
import org.http4s.Response
import org.http4s.dsl.Http4sDsl
import rescueme.com.app.domain.DomainError
import rescueme.com.app.domain.DomainError._
import rescueme.com.app.domain.dog.DogDetail

import java.util.UUID

package object endpoint {

  type ValidationResult[A] = ValidatedNec[DomainError, A]
  def withValidation[F[_]: Sync, A](
      validated: ValidationResult[A]
  )(fn: A => F[Response[F]])(implicit dsl: Http4sDsl[F]): F[Response[F]] = {
    import dsl._
    validated.toEither.fold(_ => UnprocessableEntity("Request malformed"), fn)
  }

  def sameId(id: UUID, dogDetails: DogDetail): ValidatedNec[DogNotFound.type, DogDetail] =
    Validated.condNec(dogDetails.dogId == id, dogDetails, DogNotFound)
}
