package rescueme.com.rescue_me_backoffice.entry_point

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import rescueme.com.rescue_me_backoffice.dog.domain.DogRepositoryAlgebra
import rescueme.com.rescue_me_backoffice.dog.infrastructure.repository.inmemory.DogRepositoryInMemoryInterpreter
import rescueme.com.rescue_me_backoffice.system.akka.{Controller, HttpServerConfig}
import rescueme.com.rescue_me_backoffice.system.domain.System
import rescueme.com.rescue_me_backoffice.system.repositories.SystemRepositories

import scala.concurrent.{ExecutionContextExecutor, Future}

object RescueMeBackoffice extends App {

  val appConfig = ConfigFactory.load("application")
  val httpServerConfig = HttpServerConfig(ConfigFactory.load("http-server"))
  val actorSystemName = appConfig.getString("main-actor-system.name")

  implicit val actorSystem: ActorSystem = ActorSystem(actorSystemName)
  implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher
  implicit val dogRepository: DogRepositoryAlgebra[Future] = new DogRepositoryInMemoryInterpreter
  implicit val systemRepositories: System[Future] = new SystemRepositories

  val controller = Controller()

  controller.run(httpServerConfig)


}
