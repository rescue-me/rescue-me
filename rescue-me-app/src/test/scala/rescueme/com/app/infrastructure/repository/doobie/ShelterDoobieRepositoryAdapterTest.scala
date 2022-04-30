package rescueme.com.app.infrastructure.repository.doobie

import cats.effect.{ContextShift, IO}
import com.dimafeng.testcontainers.PostgreSQLContainer
import doobie.implicits._
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfterAll, OptionValues}
import doobie.util.transactor.Transactor
import rescueme.com.app.domain.shelter.Shelter

import scala.concurrent.ExecutionContext

class ShelterDoobieRepositoryAdapterTest extends AsyncFlatSpec with Matchers with BeforeAndAfterAll with OptionValues {

  implicit private val ioContextShift: ContextShift[IO]      = IO.contextShift(ExecutionContext.global)
  private var repository: ShelterDoobieRepositoryAdapter[IO] = _
  private val container: PostgreSQLContainer                 = PostgreSQLContainer()
  private var transactor: doobie.Transactor[IO]              = _

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    container.start()
    transactor = Transactor.fromDriverManager[IO](
      "org.postgresql.Driver",
      container.jdbcUrl,
      container.username,
      container.password
    )
    sql"CREATE TABLE shelter(id serial NOT NULL,PRIMARY KEY (id),name character varying NOT NULL,province character varying NOT NULL)".update.run
      .transact(transactor)
      .unsafeRunSync()
    repository = ShelterDoobieRepositoryAdapter(transactor)
  }

  behavior of "Doobie shelter repository adapter"

  it should "retrieve all shelters ok" in {
    repository.all().unsafeRunSync().size shouldBe 0
  }

  it should "create shelter and retrieve by id ok" in {

    val shelter = Shelter("name-test", "province-test")

    (for {
      saved   <- repository.create(shelter)
      all     <- repository.all()
      getById <- repository.get(saved.id.value)
    } yield {
      all should contain(saved)
      saved shouldBe getById.value
    }).unsafeRunSync()
  }

}
