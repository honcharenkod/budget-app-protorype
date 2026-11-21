package app.budget.mono

import zio.json.ast.Json

import java.sql.Timestamp

case class RawMonoStatement(
    id: Long,
    accountId: String,
    periodFrom: Timestamp,
    periodTo: Timestamp,
    fetchedAt: Timestamp,
    payload: Json
)
