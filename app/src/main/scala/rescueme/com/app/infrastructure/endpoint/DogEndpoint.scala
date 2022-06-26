package rescueme.com.app.infrastructure.endpoint

import cats.effect.kernel.Async
import cats.syntax.all._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, QueryParamDecoder}
import rescueme.com.app.domain.dog.{Dog, DogDetail, DogDetailService, DogService}

import java.util.UUID

class DogEndpoint[F[_]: Async] {

  implicit val dsl = Http4sDsl.apply[F]
  import dsl._

  implicit val shelterIdQueryParamDecoder: QueryParamDecoder[UUID] =
    QueryParamDecoder[String].map(UUID.fromString)

  def endpoints(dogService: DogService[F], dogDetailsService: DogDetailService[F]): HttpRoutes[F] =
    findDogs(dogService) <+>
      createDog(dogService) <+>
      get(dogService) <+>
      details(dogDetailsService)

  private def findDogs(dogService: DogService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root :? ShelterFilterQueryParamMatcher(shelter) =>
        for {
          all  <- dogService.getByShelter(shelter)
          resp <- Ok(all.asJson)
        } yield resp

      case GET -> Root =>
        for {
          retrieved <- dogService.all
          resp <- Ok(retrieved.asJson).handleErrorWith { thr =>
            println(s"Error occurred $thr")
            InternalServerError()
          }
        } yield resp
    }

  private def createDog(dogService: DogService[F]): HttpRoutes[F] = {
    HttpRoutes.of[F] { case req @ POST -> Root =>
      val result = for {
        dog <- req.as[Dog]
        res <- dogService.create(dog)
      } yield res
      result.flatMap {
        case Right(dogCreated) => Ok(dogCreated.asJson)
        case Left(_)           => Conflict(s"A dog with these fields already exists")
      }
    }
  }

  private def get(dogService: DogService[F]): HttpRoutes[F] = {
    HttpRoutes.of[F] { case GET -> Root / UUIDVar(id) =>
      dogService.get(id) flatMap {
        case None      => BadRequest(s"Dog with id: $id not found")
        case Some(dog) => Ok(dog.asJson)
      }
    }
  }

  private def details(dogDetailsService: DogDetailService[F]): HttpRoutes[F] = {
    import Request._
    import Response._

    HttpRoutes.of[F] {
      case GET -> Root / UUIDVar(id) / "details" =>
        dogDetailsService.get(id) flatMap {
          case Left(_)        => BadRequest(s"Dog with id: $id not found")
          case Right(details) => Ok(details.toResponse.asJson)
        }
      case req @ POST -> Root / UUIDVar(id) / "details" =>
        for {
          dogDetails <- req.as[DogDetailsRequest].map(_.toDogDetails(id))
          created <- withValidation(sameId(id, dogDetails)) { valid =>
            dogDetailsService.create(valid) match {
              case Left                      => InternalServerError()
              case Right(created: DogDetail) => Ok(created.toResponse.asJson)
            }
          }
        } yield created
      case req @ PUT -> Root / UUIDVar(id) / "details" =>
        for {
          dogDetails <- req.as[DogDetailsRequest].map(_.toDogDetails(id))
          created <- withValidation(sameId(id, dogDetails)) { valid =>
            dogDetailsService.update(valid) match {
              case Left                      => InternalServerError()
              case Right(created: DogDetail) => Ok(created.toResponse.asJson)
            }
          }
        } yield created
    }
  }

  object ShelterFilterQueryParamMatcher extends QueryParamDecoderMatcher[UUID]("shelter")
}

object DogEndpoint {
  def endpoints[F[_]: Async](
      dogService: DogService[F],
      dogDetailsService: DogDetailService[F]
  ): HttpRoutes[F] = new DogEndpoint[F].endpoints(dogService, dogDetailsService)
}
