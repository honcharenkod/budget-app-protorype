package app.budget.mono.model.api

import zio.json.JsonDecoder

final case class StatementItem(
    id: String,
    time: Long,
    description: String,
    mcc: Int,
    originalMcc: Int,
    hold: Boolean,
    amount: Long,
    operationAmount: Long,
    currencyCode: Int, // ISO 4217
    commissionRate: Long,
    cashbackAmount: Long,
    balance: Long,
    comment: Option[String] = None,
    receiptId: Option[String] = None,
    invoiceId: Option[String] = None,
    counterEdrpou: Option[String] = None,
    counterIban: Option[String] = None,
    counterName: Option[String] = None
) derives JsonDecoder
