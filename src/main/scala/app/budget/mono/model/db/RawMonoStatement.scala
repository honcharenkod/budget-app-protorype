package app.budget.mono.model.db

import zio.json.ast.Json

import java.time.Instant

case class RawMonoStatement(
    id: Long,
    accountId: String,
    periodFrom: Instant,
    periodTo: Instant,
    fetchedAt: Instant,
    payload: Json
)

object RawMonoStatement:
  case class Insert(
      accountId: String,
      periodFrom: Instant,
      periodTo: Instant,
      payload: Json
  )
