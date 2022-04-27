package rescueme.com.app.infrastructure.repository.doobie

import cats.effect.{ContextShift, IO}
import com.dimafeng.testcontainers.{Container, ForAllTestContainer, PostgreSQLContainer}
import doobie.implicits._
import doobie.util.transactor.Transactor
import org.scalatest.{BeforeAndAfterAll, OptionValues}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import rescueme.com.app.domain.dog.Dog

import scala.concurrent.ExecutionContext

class DogDoobieRepositoryAdapterTest extends AsyncFlatSpec with Matchers with BeforeAndAfterAll with OptionValues {

  implicit private val ioContextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  private val container: PostgreSQLContainer             = PostgreSQLContainer()
  private var transactor: doobie.Transactor[IO]          = _
  private var repository: DogDoobieRepositoryAdapter[IO] = _

  override def beforeAll(): Unit = {
    container.start()
    transactor = Transactor.fromDriverManager[IO](
      "org.postgresql.Driver",
      container.jdbcUrl,
      container.username,
      container.password
    )
    repository = DogDoobieRepositoryAdapter(transactor)
    sql"CREATE TABLE dog(id serial NOT NULL,PRIMARY KEY (id),name character varying NOT NULL,breed character varying NOT NULL,description character varying NOT NULL)".update.run
      .transact(transactor)
      .unsafeRunSync()
  }

  override def afterAll(): Unit = {
    container.stop()
  }

  behavior of "Doobie dog repository adapter"

  it should "retrieve environments ok" in {

    repository.all().unsafeRunSync().size shouldBe 0
  }

  it should "insert and retrieve dog" in {

    val dog = Dog("budy-test", "tester", "testing doobie")

    (for {
      saved <- repository.create(dog)
      all   <- repository.all()
      byId  <- repository.get(saved.id.value)
    } yield {
      all should contain(saved)
      saved shouldBe byId.value
    }).unsafeRunSync()

  }

}
