package rescueme.com.app.infrastructure.endpoint

import cats.effect.Sync
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import rescueme.com.app.domain.dog.DogService

class DogEndpoint[F[_]: Sync] extends Http4sDsl[F] {

  private def findAllDogs(dogService: DogService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root => Ok()
    }

  def endpoints(
      dogService: DogService[F]
  ): HttpRoutes[F] = findAllDogs(dogService)
}

object DogEndpoint {
  def endpoints[F[_]: Sync](
      dogService: DogService[F]
  ): HttpRoutes[F] = new DogEndpoint[F].endpoints(dogService)
}
