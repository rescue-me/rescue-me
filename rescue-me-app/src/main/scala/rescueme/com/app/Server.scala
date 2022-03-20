package rescueme.com.app

import cats.effect._
import doobie.util.ExecutionContexts
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.{Router, Server => H4Server}
import rescueme.com.app.infrastructure.endpoint.DogEndpoint


object Server extends IOApp {

  def createServer[F[_] : ContextShift : ConcurrentEffect : Timer]: Resource[F, H4Server[F]] = for {
    serverEc <- ExecutionContexts.cachedThreadPool[F]
    httpApp = Router(
      "/api/dog" -> DogEndpoint.endpoints
    ).orNotFound
    server <- BlazeServerBuilder[F](serverEc)
      .bindHttp(8080, "localhost")
      .withHttpApp(httpApp)
      .resource
  } yield server

  override def run(args: List[String]): IO[ExitCode] = createServer.use(_ => IO.never).as(ExitCode.Success)

}
