package rescueme.com.app.infrastructure.repository.doobie

import rescueme.com.app.domain.shelter.ShelterRepositoryAlgebra

object ShelterSql {

}
case class ShelterDoobieRepositoryAdapter[F[_]] () extends ShelterRepositoryAlgebra[F]{
  import ShelterSql._

}
