package rescueme.com.app.infrastructure.endpoint

import cats.effect.Sync
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import rescueme.com.app.domain.shelter.ShelterService

class ShelterEndpoint[F[_]: Sync] extends Http4sDsl[F] {

  def endpoints(shelterService: ShelterService[F]): HttpRoutes[F] = ???
}
object ShelterEndpoint {
  def endpoints[F[_]: Sync](shelterService: ShelterService[F]): HttpRoutes[F] =
    new ShelterEndpoint[F].endpoints(shelterService)
}
