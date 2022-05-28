package rescueme.com.app

import cats.effect._
import doobie.util.ExecutionContexts
import io.circe.config.parser
import io.circe.generic.auto._
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.{Router, Server => H4Server}
import rescueme.com.app.config.{DatabaseConfig, RescuemeConfig}
import rescueme.com.app.domain.dog.{DogDetailService, DogService, DogValidator}
import rescueme.com.app.domain.shelter.{ShelterService, ShelterValidator}
import rescueme.com.app.infrastructure.endpoint.{DogEndpoint, ShelterEndpoint}
import rescueme.com.app.infrastructure.repository.doobie.{DogDetailRepositoryAdapter, DogDoobieRepositoryAdapter, ShelterDoobieRepositoryAdapter}

object Server extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    createServer
      .use(_ => IO.never)
      .as(ExitCode.Success)

  def createServer[F[_]: ContextShift: ConcurrentEffect: Timer]: Resource[F, H4Server[F]] =
    for {
      conf     <- Resource.eval(parser.decodePathF[F, RescuemeConfig]("application"))
      connEc   <- ExecutionContexts.fixedThreadPool[F](conf.db.connections.poolSize)
      txnEc    <- ExecutionContexts.cachedThreadPool[F]
      xa       <- DatabaseConfig.dbTransactor(conf.db, connEc, Blocker.liftExecutionContext(txnEc))
      serverEc <- ExecutionContexts.cachedThreadPool[F]
      dogRepo           = DogDoobieRepositoryAdapter[F](xa)
      shelterRepo       = ShelterDoobieRepositoryAdapter[F](xa)
      shelterValidation = ShelterValidator.make[F](shelterRepo)
      dogService        = DogService.make[F](dogRepo, shelterValidation)
      shelterService    = ShelterService.make[F](shelterRepo)
      dogValidation     = DogValidator.make[F](dogRepo)
      dogDetailRepo     = DogDetailRepositoryAdapter.make(xa)
      dogDetailService  = DogDetailService.make(dogDetailRepo, dogValidation)
      httpApp = Router(
        "/api/dog"     -> DogEndpoint.endpoints(dogService, dogDetailService),
        "/api/shelter" -> ShelterEndpoint.endpoints(shelterService)
      ).orNotFound
      server <- BlazeServerBuilder[F](serverEc)
        .bindHttp(8080, "localhost")
        .withHttpApp(httpApp)
        .resource
    } yield server

}
