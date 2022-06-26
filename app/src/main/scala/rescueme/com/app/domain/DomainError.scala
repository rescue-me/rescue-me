package rescueme.com.app.domain

import scala.util.control.NoStackTrace

sealed trait DomainError extends NoStackTrace
object DomainError {
  type DogNotFound          = DogNotFound.type
  type DogAlreadyExists     = DogAlreadyExists.type
  type DogDetailsNotUpdated = DogDetailsNotUpdated.type
  type ShelterNotFound      = ShelterNotFound.type

  case object DogNotFound             extends DomainError
  case object DogAlreadyExists        extends DomainError
  case object DogDetailsAlreadyExists extends DomainError
  case object ShelterNotFound         extends DomainError
  case object DogDetailsNotUpdated    extends DomainError
}
