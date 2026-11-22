package app.budget.mono

import app.budget.mono.model.MonoAccount
import app.budget.mono.model.domain.{NewRawMonoStatement, RawMonoStatement}
import zio.IO

import java.sql.SQLException

trait MonoRepo:
  def saveRaw(raw: NewRawMonoStatement): IO[SQLException, Long]
  def createUserAccount(account: MonoAccount): IO[SQLException, Long]
