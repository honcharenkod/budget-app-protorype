CREATE TABLE raw_mono_statement (
    id bigserial PRIMARY KEY,
    account_id text NOT NULL,
    period_from timestamptz NOT NULL,
    period_to timestamptz NOT NULL,
    fetched_at timestamptz NOT NULL DEFAULT now(),
    payload jsonb NOT NULL
);
