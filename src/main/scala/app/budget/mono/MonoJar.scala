package app.budget.mono

case class MonoJar(
    id: String,
    sendId: String,
    title: String,
    description: String,
    currencyCode: Int,
    balance: Long,
    goal: Long
)
