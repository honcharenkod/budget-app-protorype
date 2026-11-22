package app.budget.mono

import app.budget.mono.model.api.MonoAccount
import app.budget.mono.model.db.{NewRawMonoStatement, RawMonoStatement}
import zio.IO

import java.sql.SQLException

trait MonoRepo:
  def insertRawStatement(raw: NewRawMonoStatement): IO[SQLException, Long]
  def insertAccountIfAbsent(account: MonoAccount): IO[SQLException, Long]
