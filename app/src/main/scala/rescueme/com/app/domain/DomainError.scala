package rescueme.com.app.domain

sealed class DomainError

object DomainError {
  case object DogNotFound extends DomainError
  type DogNotFound = DogNotFound.type

  case object DogAlreadyExists extends DomainError
  type DogAlreadyExists = DogAlreadyExists.type
  case object DogDetailsAlreadyExists extends DomainError
  case object ShelterNotFound         extends DomainError
  case object DogDetailsNotUpdated    extends DomainError
  type DogDetailsNotUpdated = DogDetailsNotUpdated.type
}
