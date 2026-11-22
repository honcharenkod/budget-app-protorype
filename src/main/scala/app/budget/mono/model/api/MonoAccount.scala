package app.budget.mono.model.api

case class MonoAccount(
    id: String,
    `type`: String,
    currencyCode: Int,
    iban: String
)
