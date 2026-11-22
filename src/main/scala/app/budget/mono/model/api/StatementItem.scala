package app.budget.mono.model.api

import app.budget.mono.model.domain.RawMonoStatement
import zio.json.{DeriveJsonCodec, JsonCodec}
import zio.schema.{DeriveSchema, Schema}

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
)

object StatementItem:
  implicit val schema: Schema[StatementItem] =
    DeriveSchema.gen[StatementItem]

  implicit val codec: JsonCodec[StatementItem] =
    DeriveJsonCodec.gen[StatementItem]
