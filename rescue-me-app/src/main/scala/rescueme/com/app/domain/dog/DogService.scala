package rescueme.com.app.domain.dog

class DogService[F[_]](
    repository: DogRepositoryAlgebra[F]
) {

  def all(): F[List[Dog]] = repository.all()
}

object DogService {
  def apply[F[_]](
      repositoryAlgebra: DogRepositoryAlgebra[F]
  ): DogService[F] = new DogService[F](repositoryAlgebra)
}
