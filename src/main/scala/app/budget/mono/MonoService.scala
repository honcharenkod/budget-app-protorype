package app.budget.mono

import zio._

trait MonoService:
  def importAccountsAndStatements: Task[Long]
