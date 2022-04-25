package rescueme.com.app

import cats.effect._
import doobie.util.ExecutionContexts
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.{Router, Server => H4Server}
import rescueme.com.app.domain.dog.DogService
import rescueme.com.app.infrastructure.endpoint.DogEndpoint
import rescueme.com.app.infrastructure.repository.inmemory.DogRepositoryInMemoryInterpreter

object Server extends IOApp {

  def createServer[F[_]: ContextShift: ConcurrentEffect: Timer]: Resource[F, H4Server[F]] =
    for {
      serverEc <- ExecutionContexts.cachedThreadPool[F]
      dogRepo    = DogRepositoryInMemoryInterpreter[F]
      dogService = DogService(dogRepo)
      httpApp = Router(
        "/api/dog" -> DogEndpoint.endpoints(dogService)
      ).orNotFound
      server <- BlazeServerBuilder[F](serverEc)
        .bindHttp(8080, "localhost")
        .withHttpApp(httpApp)
        .resource
    } yield server

  override def run(args: List[String]): IO[ExitCode] =
    IO(println("Running server ...")) *> createServer.use(_ => IO.never).as(ExitCode.Success)

}
