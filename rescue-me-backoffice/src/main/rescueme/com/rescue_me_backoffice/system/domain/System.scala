package rescueme.com.rescue_me_backoffice.system.domain

import rescueme.com.rescue_me_backoffice.dog.domain.DogService

trait System[P[_]] {
  val dogService: DogService[P]
}
