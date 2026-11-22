package app.budget.mono

import app.budget.mono.model.api.StatementItem
import app.budget.mono.model.MonoClientInfo
import app.budget.mono.model.domain.RawMonoStatement
import app.budget.utils.TimeUtils.DateRange
import zio.Task

import java.time.Period

trait MonoClient:
  def getClientInfo: Task[MonoClientInfo]
  def getPersonalStatement(
      accountId: String,
      range: DateRange
  ): Task[List[StatementItem]]
