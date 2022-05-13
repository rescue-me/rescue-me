package rescueme.com.app.domain

sealed class DomainError
case object DogNotFound extends DomainError
case object DogAlreadyExists extends DomainError
case object DogDetailsAlreadyExists extends DomainError
case object ShelterNotFound extends DomainError
case object DogDetailsNotUpdated extends DomainError
