package rescueme.com.app.domain.dog

import cats.Monad
import cats.data.EitherT
import cats.implicits._

class DogService[F[_]: Monad](repository: DogRepositoryAlgebra[F]) {

  def all(): F[List[Dog]] =
    for {
      list <- repository.all()
      _    <- println(s"Retrieved list $list").pure[F]
    } yield list

  def create(dog: Dog): EitherT[F, Throwable, Dog] =
    EitherT.liftF(repository.create(dog))

}

object DogService {
  def apply[F[_]: Monad](repositoryAlgebra: DogRepositoryAlgebra[F]): DogService[F] =
    new DogService[F](repositoryAlgebra)
}
