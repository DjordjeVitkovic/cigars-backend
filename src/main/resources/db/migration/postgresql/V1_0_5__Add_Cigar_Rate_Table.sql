-- Create the cigarRating table
CREATE TABLE IF NOT EXISTS cigarRating
(
    cigarRateId    SERIAL PRIMARY KEY,
    cigarId        INT       NOT NULL,
    cigarRateValue INT       NOT NULL,
    created        TIMESTAMP NOT NULL,
    positive       BOOLEAN   NOT NULL,
    recommend      BOOLEAN   NOT NULL,
    comment        TEXT,

    FOREIGN KEY (cigarId) REFERENCES cigars (cigarId)
);

-- Create an index on the cigarId column for faster lookups
CREATE INDEX IF NOT EXISTS idx_cigarRating_cigarId ON cigarRating (cigarId);
