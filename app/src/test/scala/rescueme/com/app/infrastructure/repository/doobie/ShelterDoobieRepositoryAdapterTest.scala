package rescueme.com.app.infrastructure.repository.doobie

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.dimafeng.testcontainers.PostgreSQLContainer
import doobie.implicits._
import doobie.util.transactor.Transactor
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfterAll, OptionValues}
import rescueme.com.app.domain.shelter.Shelter

class ShelterDoobieRepositoryAdapterTest extends AsyncFlatSpec with Matchers with BeforeAndAfterAll with OptionValues {

  private val container: PostgreSQLContainer                 = PostgreSQLContainer()
  private var repository: ShelterDoobieRepositoryAdapter[IO] = _
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
    sql"CREATE TABLE shelter(id uuid NOT NULL,PRIMARY KEY (id),name character varying NOT NULL,province character varying NOT NULL)".update.run
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
