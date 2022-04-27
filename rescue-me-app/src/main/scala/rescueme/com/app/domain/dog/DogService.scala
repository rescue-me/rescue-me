package rescueme.com.app.domain.dog

import cats.Monad
import cats.data.EitherT
import rescueme.com.app.domain.DogNotFound

class DogService[F[_]: Monad](repository: DogRepositoryAlgebra[F]) {

  def all(): F[List[Dog]]                              = repository.all()
  def create(dog: Dog): EitherT[F, Throwable, Dog]     = EitherT.liftF(repository.create(dog))
  def get(id: Long): EitherT[F, DogNotFound.type, Dog] = EitherT.fromOptionF(repository.get(id), DogNotFound)

}

object DogService {
  def apply[F[_]: Monad](repositoryAlgebra: DogRepositoryAlgebra[F]): DogService[F] =
    new DogService[F](repositoryAlgebra)
}
