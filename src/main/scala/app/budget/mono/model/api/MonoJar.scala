package app.budget.mono.model.api

import zio.json.JsonDecoder

case class MonoJar(
    id: String,
    sendId: String,
    title: String,
    description: String,
    currencyCode: Int,
    balance: Long,
    goal: Long
) derives JsonDecoder
