package db

import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill
import zio.{RIO, ZLayer}

final case class TransactorLive(quill: Quill.Postgres[SnakeCase]) extends Transactor:
  def transact[R, A](zio: RIO[R, A]): RIO[R, A] =
    quill.transaction(zio)

object TransactorLive:
  val layer: ZLayer[Quill.Postgres[SnakeCase], Nothing, Transactor] =
    ZLayer.fromFunction(TransactorLive(_))
