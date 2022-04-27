package rescueme.com.app.infrastructure.endpoint

import cats.effect.Sync
import cats.syntax.all._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes}
import rescueme.com.app.domain.dog.{Dog, DogService}

class DogEndpoint[F[_]: Sync] extends Http4sDsl[F] {

  implicit val dogDecoder: EntityDecoder[F, Dog] = jsonOf

  private def findAllDogs(dogService: DogService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root =>
        for {
          retrieved <- dogService.all()
          _         <- println(s"retrieved dogs: $retrieved").pure[F]
          resp <- Ok(retrieved.asJson).handleErrorWith { thr =>
            println(s"Error occured $thr")
            InternalServerError()
          }
        } yield resp
    }

  private def createDog(dogService: DogService[F]): HttpRoutes[F] = {
    HttpRoutes.of[F] {
      case req @ POST -> Root =>
        val result = for {
          dog <- req.as[Dog]
          res <- dogService.create(dog).value
        } yield res
        result.flatMap {
          case Right(dogCreated) => Ok(dogCreated.asJson)
          case Left(_)           => Conflict(s"A dog with these fields already exists")
        }
    }
  }

  private def get(dogService: DogService[F]): HttpRoutes[F] = {
    HttpRoutes.of[F] {
      case GET -> Root / LongVar(id) =>
        dogService.get(id).value flatMap {
          case Left(_)    => BadRequest(s"Dog with id: $id not found")
          case Right(dog) => Ok(dog.asJson)
        }
    }
  }

  def endpoints(
      dogService: DogService[F]
  ): HttpRoutes[F] =
    findAllDogs(dogService) <+>
      createDog(dogService) <+>
      get(dogService)
}

object DogEndpoint {
  def endpoints[F[_]: Sync](
      dogService: DogService[F]
  ): HttpRoutes[F] = new DogEndpoint[F].endpoints(dogService)
}
