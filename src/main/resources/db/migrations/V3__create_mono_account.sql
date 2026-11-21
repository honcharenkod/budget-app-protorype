CREATE TABLE mono_account (
    id text PRIMARY KEY,
    type text NOT NULL,
    currency_code int NOT NULL,
    iban text NOT NULL
);
