package rescueme.com.app.infrastructure.endpoint

import cats.effect.Sync
import org.http4s.dsl.Http4sDsl

class ShelterEndpoint[F[_]: Sync] extends Http4sDsl[F] {}
