package rescueme.com.app.infrastructure.endpoint

import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.{EntityDecoder, EntityEncoder, HttpApp, Uri}
import org.scalatest.OptionValues
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import rescueme.com.app.Arbitraries
import rescueme.com.app.domain.dog.{Dog, DogRepositoryAlgebra, DogService}
import rescueme.com.app.domain.shelter.{ShelterValidation, ShelterValidationInterpreter}
import rescueme.com.app.infrastructure.repository.{DogStubRepository, ShelterStubRepository}

import java.util.UUID

class DogEndpointTest
    extends AnyFunSuite
    with Matchers
    with Http4sDsl[IO]
    with Http4sClientDsl[IO]
    with Arbitraries
    with ScalaCheckPropertyChecks
    with OptionValues {

  implicit val entityDecoder: EntityDecoder[IO, Dog] = jsonOf[IO, Dog]
  implicit val entityEncoder: EntityEncoder[IO, Dog] = jsonEncoderOf[IO, Dog]
  val dogRepo: DogRepositoryAlgebra[IO]              = DogStubRepository
  val shelterValidation: ShelterValidation[IO]       = ShelterValidationInterpreter[IO](ShelterStubRepository)
  val dogService: DogService[IO]                     = DogService(dogRepo, shelterValidation)
  val router: HttpApp[IO]                            = Router("/dogs" -> DogEndpoint.endpoints[IO](dogService)).orNotFound

  test("Should return ok") {
    forAll { (dog: Dog) =>
      (for {
        createReq  <- POST(dog, uri"/dogs")
        createRes  <- router.run(createReq)
        createdDog <- createRes.as[Dog]
        req        <- GET(uri"/dogs")
        resp       <- router.run(req)
        body       <- resp.as[List[Dog]]
      } yield {
        createRes.status shouldEqual Ok
        resp.status shouldEqual Ok
        body.size should be > 0
      }).unsafeRunSync()
    }
  }

  test("Should create and retrieve by id") {
    forAll { dog: Dog =>
      (for {
        createReq  <- POST(dog, uri"/dogs")
        createRes  <- router.run(createReq)
        createdDog <- createRes.as[Dog]
        req        <- GET(Uri.unsafeFromString(s"/dogs/${createdDog.id.value}"))
        resp       <- router.run(req)
        body       <- resp.as[Dog]
      } yield {
        body shouldBe createdDog
      }).unsafeRunSync()
    }
  }

  test("Should retrieve by shelter") {
    (for {
      req      <- GET(Uri.unsafeFromString(s"/dogs?shelter=${UUID.randomUUID()}"))
      res      <- router.run(req)
      response <- res.as[List[Dog]]
    } yield {
      response.size should be > 0
    }).unsafeRunSync()
  }
}
