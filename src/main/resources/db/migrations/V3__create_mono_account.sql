CREATE TABLE mono_account (
    id          text PRIMARY KEY,
    type text NOT NULL,
    currencyCode int NOT NULL,
    iban text NOT NULL
);
