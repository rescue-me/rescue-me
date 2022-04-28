package rescueme.com.app.domain

sealed class DomainError
case object DogNotFound extends DomainError
case object ShelterNotFound extends DomainError
