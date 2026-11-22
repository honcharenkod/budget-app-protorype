--add new field and set it up with hashed value
ALTER TABLE raw_mono_statement
ADD payload_hash text GENERATED ALWAYS AS (md5(payload::text)) STORED;

--check for hash duplicates and persist only the last ones
WITH sorted_by_date AS (
    SELECT
        id,
        row_number() OVER (
            PARTITION BY account_id, period_from, period_to, payload_hash
            ORDER BY fetched_at DESC, id DESC
        ) AS rn
    FROM raw_mono_statement
)

DELETE FROM raw_mono_statement
USING sorted_by_date
WHERE
    raw_mono_statement.id = sorted_by_date.id
    AND sorted_by_date.rn > 1;

--add unique constraint
ALTER TABLE raw_mono_statement
ADD CONSTRAINT raw_mono_statement_dedup_key UNIQUE (account_id, period_from, period_to, payload_hash);
