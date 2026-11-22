package app.budget.mono

import zio.{IO, Task}

import java.sql.SQLException

trait MonoService:
  def importAccountsAndStatements: Task[Long]
