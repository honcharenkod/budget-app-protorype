package app.budget.mono.api

import app.budget.domain.DateRange
import app.budget.mono.model.api.{MonoClientInfo, StatementItem}
import zio.IO

trait MonoClient:
  def getClientInfo: IO[MonoApiError, MonoClientInfo]
  def getPersonalStatement(
      accountId: String,
      range: DateRange
  ): IO[MonoApiError, List[StatementItem]]
