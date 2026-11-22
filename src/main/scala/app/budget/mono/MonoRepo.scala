package app.budget.mono

import app.budget.mono.model.{MonoAccount, RawMonoStatement}
import zio.IO

import java.sql.SQLException

trait MonoRepo:
  def saveRaw(raw: RawMonoStatement): IO[SQLException, Long]
  def createUserAccount(account: MonoAccount): IO[SQLException, Long]
