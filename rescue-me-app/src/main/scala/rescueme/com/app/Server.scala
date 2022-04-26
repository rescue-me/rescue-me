package rescueme.com.app

import cats.effect._
import doobie.util.ExecutionContexts
import io.circe.config.parser
import io.circe.generic.auto._
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.{Router, Server => H4Server}
import rescueme.com.app.config.{DatabaseConfig, RescuemeConfig}
import rescueme.com.app.domain.dog.DogService
import rescueme.com.app.infrastructure.endpoint.DogEndpoint
import rescueme.com.app.infrastructure.repository.doobie.DogDoobieRepositoryAdapter

object Server extends IOApp {

  def createServer[F[_]: ContextShift: ConcurrentEffect: Timer]: Resource[F, H4Server[F]] =
    for {
      conf     <- Resource.eval(parser.decodePathF[F, RescuemeConfig]("application"))
      serverEc <- ExecutionContexts.cachedThreadPool[F]
      connEc   <- ExecutionContexts.fixedThreadPool[F](conf.db.connections.poolSize)
      txnEc    <- ExecutionContexts.cachedThreadPool[F]
      xa       <- DatabaseConfig.dbTransactor(conf.db, connEc, Blocker.liftExecutionContext(txnEc))
      serverEc <- ExecutionContexts.cachedThreadPool[F]
      dogRepo    = DogDoobieRepositoryAdapter[F](xa)
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
    createServer
      .use(_ => IO.never)
      .as(ExitCode.Success)

}
