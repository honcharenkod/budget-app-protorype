package app.budget.mono.model

case class MonoAccount(
    id: String,
    `type`: String,
    currencyCode: Int,
    iban: String
)
