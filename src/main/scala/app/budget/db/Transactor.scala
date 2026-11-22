package app.budget.db

import zio.*

trait Transactor:
  def transact[R, A](zio: RIO[R, A]): RIO[R, A]

object Transactor:
  extension [R, A](zio: RIO[R, A])
    def transactional(using t: Transactor): RIO[R, A] = t.transact(zio)
