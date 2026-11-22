package app.budget.mono

import app.budget.domain.DateRange
import app.budget.mono.model.api.{MonoClientInfo, StatementItem}
import app.budget.mono.model.db.RawMonoStatement
import zio.Task

import java.time.Period

trait MonoClient:
  def getClientInfo: Task[MonoClientInfo]
  def getPersonalStatement(
      accountId: String,
      range: DateRange
  ): Task[List[StatementItem]]
