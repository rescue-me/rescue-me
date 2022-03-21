package rescueme.com.app.domain.dog

import cats.Functor
import cats.data.EitherT

class DogService[F[_]](
    repository: DogRepositoryAlgebra[F]
) {

  def all(): F[List[Dog]] =
    repository.all()

  def create(dog: Dog)(implicit f: Functor[F]): EitherT[F, Throwable, Dog] =
    EitherT.liftF(repository.create(dog))
}

object DogService {
  def apply[F[_]](
      repositoryAlgebra: DogRepositoryAlgebra[F]
  ): DogService[F] = new DogService[F](repositoryAlgebra)
}
