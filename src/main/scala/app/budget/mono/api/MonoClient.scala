package app.budget.mono.api

import app.budget.domain.DateRange
import app.budget.mono.model.api.MonoClientInfo
import zio.IO
import zio.json.ast.Json

trait MonoClient:
  def getClientInfo: IO[MonoApiError, MonoClientInfo]
  def getPersonalStatement(
      accountId: String,
      range: DateRange
  ): IO[MonoApiError, Json]
