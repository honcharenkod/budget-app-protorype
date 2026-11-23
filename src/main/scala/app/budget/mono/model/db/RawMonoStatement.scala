package app.budget.mono.model.db

import io.getquill.JsonbValue
import zio.json.ast.Json

import java.time.Instant

case class RawMonoStatement(
    id: Long,
    accountId: String,
    periodFrom: Instant,
    periodTo: Instant,
    fetchedAt: Instant,
    payload: JsonbValue[Json]
)

object RawMonoStatement:
  case class Insert(
      accountId: String,
      periodFrom: Instant,
      periodTo: Instant,
      payload: JsonbValue[Json]
  )
