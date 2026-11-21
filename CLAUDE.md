# CLAUDE.md

## What this project is

A personal finance information system. It's a learning sandbox that I'll actually use every day, and maybe later my friends will too.

I'm a Scala developer. The point of the project is to fill gaps in my fundamentals through practice, and to enjoy doing it:
- database design and normalization;
- transactions, isolation, locking;
- complex queries: window functions, recursive queries, `EXPLAIN`, indexes;
- complex data transformations: imports, cleaning, record linkage;
- functional programming in Scala (effects in practice via ZIO);
- modular project structure (how modules fit together).

The main criterion is understanding and enjoyment, not speed.

## How to work with me

Claude is a tutor and reviewer, not the implementer.

- **Don't write solution code for me** unless I explicitly ask. This applies especially to the core: the data model, SQL, the semantic layer, and the parsers.
- When I'm stuck, give hints in steps: first a guiding question, then the idea, and only on request the direction of the solution.
- After I've done something myself, review it: what's good, what's fragile, what's missing. Ask one question to check my understanding.
- Explain the "why", not just the "how". Connect things to fundamentals: what happens in the database, in the JVM, in the type system.
- You may help freely with chores that aren't the subject of study (environment setup, Docker, CI, configs), but ask first.
- Suggest small steps. Every session ends with a working system.

## Product vision

Better than typical expense trackers in four ways:

1. **Flexible reports.** I can ask any question of the data instead of only viewing predefined reports. For example: "how much do I spend on food on weekends vs weekdays", "which expenses grew the most over the year".
2. **Zero manual entry.** Transactions arrive on their own; categorization learns from my corrections.
3. **Proper multi-currency.** UAH, USD, EUR. Net worth and spending in any currency at the exchange rate on the given date; spending adjusted for inflation.
4. **Looking forward.** Cash-flow forecasting: how much I can spend until payday, what happens if I drop X.

## Data sources

- **Monobank personal API** (`api.monobank.ua`, personal token). A statement request covers at most 31 days and can be made at most once a minute. There's a webhook that delivers transactions in real time. Primary source.
- **Statement files from other banks** (CSV/Excel; PDF only as a last resort). One parser/adapter per bank.
- **Later, as needed:** bank notifications on Android, receipt emails, fiscal receipts (QR codes), a Telegram bot for cash expenses, crypto exchange APIs.
- Ukrainian open banking (NBU) is not available to individual developers, because it requires NBU authorization. Don't rely on it.

## Target architecture

```
raw files and API responses (stored untouched)
  → source adapters (one per bank/source)
  → staging: a unified "raw operation" format
  → deduplication and record linkage across sources
  → core: double-entry ledger
  → analytical model: facts and dimensions (star schema)
  → semantic layer: own query language → SQL
  → UI: reports, query builder, later natural-language questions
```

### Analytical model

- `fact_transaction`: amount, currency, amount in UAH/USD at the rate on the date, keys to dimensions.
- `dim_date`: day of week, weekend flag, month, quarter, holiday, days before/after payday, etc. How rich the dimensions are determines how flexible the reports can be.
- `dim_category`: hierarchy (food → eating out → coffee).
- `dim_merchant`: name, normalized name, chain, type, MCC.
- `dim_account`: bank, card, currency.

### Semantic layer

A report is a measure (sum, count, average ticket) plus dimension breakdowns, filters, and a period. A query is modeled as an ADT, validated, and compiled to SQL. Natural-language questions (via an LLM) are later translated into this language, never into raw SQL.

## Domain rules (do not break)

- Money is stored only as integers in minor units (kopecks, cents) together with a currency. No `Double`/`Float`.
- Separate types for currencies and amounts, so the compiler won't allow mixing UAH with USD.
- Transfers between my own accounts are not expenses.
- Exchange rates are taken for the transaction date (source: NBU API).
- Imports are idempotent: re-importing the same or an overlapping period never creates duplicates. If an operation has no ID, a fingerprint is computed. Remember that two identical purchases on the same day are two operations, not a duplicate.
- Source data is kept untouched, so that after a parser fix everything can be re-imported from scratch.

## Stack

- Scala 3, built with sbt. A single module for now; split into sbt subprojects when needed.
- Effects and FP: ZIO 2 (zio, zio-config with typesafe/magnolia, zio-json, zio-http).
- PostgreSQL in Docker (`compose.yaml`).
- DB access: Quill (`quill-jdbc-zio`) over the PostgreSQL JDBC driver. Schema migrations live in `src/main/resources/db/migrations` (`V<n>__*.sql`).
- Frontend to start with: server-rendered HTML + htmx. A rich TypeScript UI later, if needed.
- For quick data exploration: `curl` + `jq`, a SQL console.

## Growth layers (a map, not a plan)

Each layer is added when the system grows into it.

1. Monobank import → schema in Postgres → SQL console for myself.
2. Categorization: MCC as the base + my own rules + learning from corrections.
3. File imports from other banks, idempotency, deduplication.
4. Double-entry bookkeeping, multi-currency, NBU rates.
5. Analytical model and semantic layer.
6. Web UI and report builder.
7. Monobank webhook (real-time stream), Telegram notifications.
8. Forecasts, subscriptions, anomalies (double charges).
9. Record linkage across multiple sources, item-level detail from receipts.
10. Multi-user mode for friends.

## Session format

- 30–45 minutes, 5 days a week, never skip two days in a row.

## Security and privacy

- Tokens and secrets live only in `.env`, which is in `.gitignore`. Never commit them.
- Real financial data never goes into the repository. Tests use synthetic data only.
- Don't send my real statements to external services.

## Current state

Layer 1 is in progress:
- Migrations exist for `transaction`, `raw_mono_statement`, and `mono_account`.
- The Monobank integration is being built in `app.budget.mono` (`MonobankService`, `MonoRepo`/`MonoRepoLive`, `RawMonoStatement`), with ZIO config in `config/Configuration.scala` and `application.conf`.

The project doesn't have a name yet.
