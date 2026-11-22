package app.budget.mono.model.db

import zio.json.ast.Json

import java.time.Instant

case class NewRawMonoStatement(
    accountId: String,
    periodFrom: Instant,
    periodTo: Instant,
    payload: Json
)
