package rescueme.com.app

import cats.effect._
import com.comcast.ip4s.IpLiteralSyntax
import doobie.util.ExecutionContexts
import io.circe.config.parser
import io.circe.generic.auto._
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.{Router, Server => H4Server}
import rescueme.com.app.config.{DatabaseConfig, RescuemeConfig}
import rescueme.com.app.domain.dog.{DogDetailService, DogService, DogValidator}
import rescueme.com.app.domain.shelter.{ShelterService, ShelterValidator}
import rescueme.com.app.infrastructure.endpoint.{DogEndpoint, ShelterEndpoint}
import rescueme.com.app.infrastructure.repository.doobie.{DogDetailRepositoryAdapter, DogDoobieRepositoryAdapter, ShelterDoobieRepositoryAdapter}

object Server extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    createServer[IO].useForever
      .as(ExitCode.Success)

  def createServer[F[_]: Async]: Resource[F, H4Server] =
    for {
      conf     <- Resource.eval(parser.decodePathF[F, RescuemeConfig]("application"))
      connEc   <- ExecutionContexts.fixedThreadPool[F](conf.db.connections.poolSize)
      txnEc    <- ExecutionContexts.cachedThreadPool[F]
      xa       <- DatabaseConfig.dbTransactor(conf.db, connEc)
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
      server <- EmberServerBuilder
        .default[F]
        .withHost(host"localhost")
        .withPort(port"8080")
        .withHttpApp(httpApp)
        .build
    } yield server

}
