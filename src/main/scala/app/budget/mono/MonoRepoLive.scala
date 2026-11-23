package app.budget.mono

import app.budget.db.TableName
import app.budget.mono.model.api.MonoAccount
import app.budget.mono.model.db.RawMonoStatement
import io.getquill.jdbczio.Quill
import zio.{IO, ZIO, ZLayer}
import io.getquill.*
import org.postgresql.util.PGobject
import zio.json.ast.Json

import java.sql.{SQLException, Types}

final case class MonoRepoLive(quill: Quill.Postgres[SnakeCase])
    extends MonoRepo:
  import quill._

  private given Encoder[Json] =
    encoder(
      Types.OTHER,
      (index, value, row) =>
        val pg = PGobject()
        pg.setType("jsonb")
        pg.setValue(value.toString)
        row.setObject(index, pg)
    )

//  private given Decoder[Json] =
//    decoder((index, row, _) =>
//      row
//        .getString(index)
//        .fromJson[Json]
//        .fold(err => throw IllegalStateException(err), identity)
//    )

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
