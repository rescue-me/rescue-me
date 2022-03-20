package rescueme.com.app.infrastructure.endpoint

import org.http4s.implicits._
import cats.effect.IO
import org.http4s.{HttpApp, Uri}
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.server.Router
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import rescueme.com.app.domain.dog.DogService
import rescueme.com.app.infrastructure.inmemory.DogRepositoryInMemoryInterpreter

class DogEndpointSpec extends AnyFunSuite with Matchers with Http4sDsl[IO] with Http4sClientDsl[IO] {

  val dogRepo: DogRepositoryInMemoryInterpreter[IO] = DogRepositoryInMemoryInterpreter[IO]
  val dogService: DogService[IO]                    = DogService(dogRepo)
  val router: HttpApp[IO]                           = Router("/dogs" -> DogEndpoint.endpoints[IO](dogService)).orNotFound

  test("Should return ok") {
    for {
      getRequest  <- GET(uri"/api/dogs")
      getResponse <- router.run(getRequest)
    } yield {
      getResponse.status shouldEqual Ok
    }

  }
}
