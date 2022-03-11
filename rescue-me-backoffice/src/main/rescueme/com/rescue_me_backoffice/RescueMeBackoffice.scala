package rescueme.com.rescue_me_backoffice

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import rescueme.com.rescue_me_backoffice.entry_point.{Controller, HttpServerConfig}

import scala.concurrent.ExecutionContextExecutor

object RescueMeBackoffice extends App {

  val appConfig = ConfigFactory.load("application")
  val httpServerConfig = HttpServerConfig(ConfigFactory.load("http-server"))
  val actorSystemName = appConfig.getString("main-actor-system.name")

  implicit val actorSystem: ActorSystem = ActorSystem(actorSystemName)
  implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

  val controller = Controller()

  controller.run(httpServerConfig)


}
