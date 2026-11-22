package app.budget.mono

import app.budget.domain.DateRange
import app.budget.mono.model.api.{MonoClientInfo, StatementItem}
import zio.Task

trait MonoClient:
  def getClientInfo: Task[MonoClientInfo]
  def getPersonalStatement(
      accountId: String,
      range: DateRange
  ): Task[List[StatementItem]]
