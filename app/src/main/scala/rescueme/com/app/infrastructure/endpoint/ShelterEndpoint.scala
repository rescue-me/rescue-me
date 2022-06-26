package rescueme.com.app.infrastructure.endpoint

import cats.effect.kernel.Async
import cats.syntax.all._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes}
import org.typelevel.log4cats.Logger
import rescueme.com.app.domain.shelter.{Shelter, ShelterService}

class ShelterEndpoint[F[_]: Async: Logger] extends Http4sDsl[F] {

  implicit val shelterDecoder: EntityDecoder[F, Shelter] = jsonOf

  def endpoints(shelterService: ShelterService[F]): HttpRoutes[F] =
    findAllShelters(shelterService) <+>
      createShelter(shelterService) <+>
      get(shelterService)

  private def findAllShelters(shelterService: ShelterService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] { case GET -> Root =>
      for {
        all  <- shelterService.all
        resp <- Ok(all.asJson)
      } yield resp
    }

  private def createShelter(shelterService: ShelterService[F]): HttpRoutes[F] = {
    HttpRoutes.of[F] { case req @ POST -> Root =>
      val result = for {
        shelter <- req.as[Shelter]
        res     <- shelterService.create(shelter)
      } yield res
      result
        .flatMap(r => Ok(r.asJson))
        .handleErrorWith(thr =>
          Logger[F].error(thr)("Error creating shelter") *> Conflict(
            s"A shelter with these fields already exists"
          )
        )
    }
  }

  private def get(shelterService: ShelterService[F]): HttpRoutes[F] = {
    HttpRoutes.of[F] { case GET -> Root / UUIDVar(id) =>
      shelterService.get(id) flatMap {
        case None          => BadRequest(s"Shelter with id: $id not found")
        case Some(shelter) => Ok(shelter.asJson)
      }
    }
  }

}
object ShelterEndpoint {
  def endpoints[F[_]: Async: Logger](shelterService: ShelterService[F]): HttpRoutes[F] =
    new ShelterEndpoint[F].endpoints(shelterService)
}
