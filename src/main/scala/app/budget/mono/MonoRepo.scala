package app.budget.mono

import zio.IO

import java.sql.SQLException

trait MonoRepo:
  def createUserAccount(account: MonoAccount): IO[SQLException, Long]
  def saveRaw(raw: RawMonoStatement): IO[SQLException, Long]
