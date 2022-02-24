package rescueme.com.rescue_me_backoffice.entry_point.endpoint

import cats.effect.Sync
import org.http4s.dsl.Http4sDsl

import scala.language.higherKinds

class DogsEndpoint[F[_] : Sync] extends Http4sDsl[F] {

}
