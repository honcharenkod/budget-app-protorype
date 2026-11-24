package app.budget.mono.model.api

import zio.json.JsonDecoder

case class MonoAccount(
    id: String,
    `type`: String,
    currencyCode: Int,
    iban: String
) derives JsonDecoder
