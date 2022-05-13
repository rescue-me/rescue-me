package rescueme.com.app.infrastructure.repository

package object doobie {
  def affectedToOption(n: Int): Option[Unit] =
    if (n > 0) Some(()) else None
}
