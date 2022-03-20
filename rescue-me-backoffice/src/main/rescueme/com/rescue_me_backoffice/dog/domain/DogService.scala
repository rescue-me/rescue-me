package rescueme.com.rescue_me_backoffice.dog.domain

import cats.Functor
import cats.data.EitherT

class DogService[F[_]](repositoryAlgebra: DogRepositoryAlgebra[F]) {

  def register(dog: Dog)(implicit M: Functor[F]): EitherT[F, Throwable, Dog] =
    EitherT.liftF(repositoryAlgebra.register(dog))

  def all(): F[List[Dog]] = repositoryAlgebra.list()
}
