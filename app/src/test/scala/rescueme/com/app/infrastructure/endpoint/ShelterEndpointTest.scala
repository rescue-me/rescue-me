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
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import rescueme.com.app.domain.shelter.{Shelter, ShelterService}
import rescueme.com.app.infrastructure.repository.ShelterStubRepository
import org.scalacheck.ScalacheckShapeless._
import org.scalatest.funsuite.AnyFunSuite

class ShelterEndpointTest
    extends AnyFunSuite
    with Matchers
      with ScalaCheckPropertyChecks
    with Http4sDsl[IO]
    with Http4sClientDsl[IO]
    with OptionValues {

  implicit val entityDecoder: EntityDecoder[IO, Shelter] = jsonOf[IO, Shelter]
  implicit val entityEncoder: EntityEncoder[IO, Shelter] = jsonEncoderOf[IO, Shelter]
  val service: ShelterService[IO]                        = ShelterService.make[IO](ShelterStubRepository)
  val router: HttpApp[IO]                                = Router("/shelter" -> ShelterEndpoint.endpoints[IO](service)).orNotFound


  test( "create new shelter ok and retrieve it" ) {
    forAll {
      shelter: Shelter =>
        (for {
          createReq  <- POST(shelter, uri"/shelter")
          createRes  <- router.run(createReq)
          createdDog <- createRes.as[Shelter]
          reqById    <- GET(Uri.unsafeFromString(s"/shelter/${createdDog.id.value}"))
          resById    <- router.run(reqById)
          body       <- resById.as[Shelter]
        } yield {
          createdDog shouldBe body
        }).unsafeRunSync()
    }

  }

  test( "retrieve shelters") {
        (for {
          listReq  <- GET( uri"/shelter")
          listRes  <- router.run(listReq)
          allShelters <- listRes.as[List[Shelter]]
        } yield {
          allShelters.size should be > 0
        }).unsafeRunSync()
  }
}
