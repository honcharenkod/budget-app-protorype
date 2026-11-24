package app.budget.mono

import app.budget.db.TableName
import app.budget.mono.model.api.MonoAccount
import app.budget.mono.model.db.RawMonoStatement
import io.getquill.*
import io.getquill.jdbczio.Quill
import zio.{IO, ZIO, ZLayer}

import java.sql.SQLException

final case class MonoRepoLive(quill: Quill.Postgres[SnakeCase])
    extends MonoRepo:
  import quill.*

  def insertRawStatement(raw: RawMonoStatement.Insert): IO[SQLException, Long] =
    run(
      querySchema[RawMonoStatement.Insert](TableName.of[RawMonoStatement])
        .insertValue(lift(raw))
        .onConflictIgnore
    )

  def insertAccountIfAbsent(account: MonoAccount): IO[SQLException, Long] =
    run(
      query[MonoAccount]
        .insertValue(lift(account))
        .onConflictIgnore(_.id)
    )

object MonoRepoLive:
  val layer: ZLayer[Quill.Postgres[SnakeCase], Nothing, MonoRepo] =
    ZLayer.fromFunction(MonoRepoLive(_))
