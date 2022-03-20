package rescueme.com.rescue_me_backoffice.system.repositories

import rescueme.com.rescue_me_backoffice.dog.domain.{Dog, DogRepositoryAlgebra, DogService}
import rescueme.com.rescue_me_backoffice.system.domain.System

class SystemRepositories[P[_]](
                                implicit dogRepositoryAlgebra: DogRepositoryAlgebra[P]
                              ) extends System[P] {
  override val dogService: DogService[P] = new DogService[P](dogRepositoryAlgebra)
}
