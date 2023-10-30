CREATE SEQUENCE IF NOT EXISTS tokens_seq;

-- Create the 'tokens' table
CREATE TABLE IF NOT EXISTS tokens (
                        id BIGSERIAL PRIMARY KEY,
                        token VARCHAR(1000) NOT NULL,
                        tokenType VARCHAR(50),
                        expired BOOLEAN NOT NULL,
                        revoked BOOLEAN NOT NULL,
                        userId BIGINT REFERENCES users(userId),
                        createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create an index on the 'user_id' column for better performance
CREATE INDEX IF NOT EXISTS idx_tokens_user ON tokens(userId);
CREATE INDEX IF NOT EXISTS idx_token_token ON tokens(token);

