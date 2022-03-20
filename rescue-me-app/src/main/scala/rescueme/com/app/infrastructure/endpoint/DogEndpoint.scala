package rescueme.com.app.infrastructure.endpoint

import cats.effect.Sync
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class DogEndpoint[F[_]: Sync] extends Http4sDsl[F] {

  def endpoints: HttpRoutes[F] = {
    HttpRoutes.of[F] {
      case GET -> Root => Ok()
    }
  }
}

object DogEndpoint {
  def endpoints[F[_]: Sync]: HttpRoutes[F] = new DogEndpoint[F]().endpoints
}