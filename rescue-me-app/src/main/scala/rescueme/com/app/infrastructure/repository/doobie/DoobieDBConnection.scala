package rescueme.com.app.infrastructure.repository.doobie

import cats.effect.{Async, ContextShift}
import com.typesafe.config.Config
import doobie.Transactor
import doobie.util.transactor.Transactor.Aux

final case class DoobieDbConnection[P[_]: Async: ContextShift](dbConfig: JdbcConfig) {

  val transactor: Aux[P, Unit] = Transactor.fromDriverManager[P](
    dbConfig.driver,
    dbConfig.url,
    dbConfig.user,
    dbConfig.password
  )
}

final case class JdbcConfig(driver: String, url: String, user: String, password: String)
object JdbcConfig {
  def apply(config: Config): JdbcConfig = JdbcConfig(
    driver = config.getString("driver"),
    url = config.getString("url"),
    user = config.getString("user"),
    password = config.getString("password")
  )
}
