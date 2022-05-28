package rescueme.com.app.infrastructure.repository.doobie

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.dimafeng.testcontainers.PostgreSQLContainer
import doobie.implicits._
import doobie.util.transactor.Transactor
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfterAll, OptionValues}
import rescueme.com.app.domain.dog.Dog

import java.util.UUID
import scala.concurrent.ExecutionContext

class DogDoobieRepositoryAdapterTest extends AsyncFlatSpec with Matchers with BeforeAndAfterAll with OptionValues {

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
    sql"""
         CREATE TABLE dog(
             id uuid NOT NULL,PRIMARY KEY (id),
             name character varying NOT NULL,
             breed character varying NOT NULL,
             description character varying NOT NULL, 
             shelter_id uuid NOT NULL )
             """.update.run
      .transact(transactor)
      .unsafeRunSync()
  }

  override def afterAll(): Unit = {
    container.stop()
  }

  behavior of "Doobie dog repository adapter"

  it should "retrieve dogs ok" in {

    repository.all().unsafeRunSync().size shouldBe 0
  }

  it should "insert and retrieve dog" in {

    val dog = Dog("budy-test", "tester", "testing doobie", UUID.randomUUID())

    (for {
      saved <- repository.create(dog)
      all   <- repository.all()
      byId  <- repository.get(saved.id.value)
    } yield {
      all should contain(saved)
      saved shouldBe byId.value
    }).unsafeRunSync()

  }

  it should "retrieve by shelter id ok" in {
    val uuid = UUID.randomUUID()
    val dog  = Dog("budy-test", "tester", "testing doobie", uuid)

    (for {
      saved <- repository.create(dog)
      all   <- repository.getByShelter(uuid)
    } yield {
      all should contain(saved)
    }).unsafeRunSync()

  }

}
