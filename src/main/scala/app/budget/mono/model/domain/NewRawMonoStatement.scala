package app.budget.mono.model.domain

import zio.json.ast.Json

import java.time.Instant

case class NewRawMonoStatement(
    accountId: String,
    periodFrom: Instant,
    periodTo: Instant,
    payload: Json
)
