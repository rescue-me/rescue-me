package rescueme.com.app.infrastructure.endpoint

import cats.effect.IO
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
import org.http4s.circe.jsonOf
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.{EntityDecoder, HttpApp, Response}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import rescueme.com.app.domain.dog.{Dog, DogService}
import rescueme.com.app.infrastructure.inmemory.DogRepositoryInMemoryInterpreter

class DogEndpointSpec extends AnyFunSuite with Matchers with Http4sDsl[IO] with Http4sClientDsl[IO] {

  implicit val decoder: Decoder[Dog] = deriveDecoder
  implicit val entityDecoder: EntityDecoder[IO, Dog] = jsonOf[IO, Dog]
  val dogRepo: DogRepositoryInMemoryInterpreter[IO]  = DogRepositoryInMemoryInterpreter[IO]
  val dogService: DogService[IO]                     = DogService(dogRepo)
  val router: HttpApp[IO]                            = Router("/dogs" -> DogEndpoint.endpoints[IO](dogService)).orNotFound

  test("Should return ok") {

    val response: Response[IO] = GET(uri"/dogs").flatMap(router.run(_)).unsafeRunSync()
    response.status shouldEqual Ok
    val dogList: List[Dog] = response.as[List[Dog]].unsafeRunSync()
    println(dogList)

  }
}
