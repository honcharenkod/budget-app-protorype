package app.budget.mono

import zio.*

trait MonoService:
  def importAccountsAndStatements: Task[Long]
