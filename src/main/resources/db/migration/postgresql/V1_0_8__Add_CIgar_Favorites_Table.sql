-- Create the 'cigar_favorites' table
CREATE TABLE IF NOT EXISTS favorites
(
    favoriteId SERIAL PRIMARY KEY,
    userId     BIGINT NOT NULL,
    cigarId    BIGINT NOT NULL,
    created    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES users (userId),
    FOREIGN KEY (cigarId) REFERENCES cigars (cigarId)
);

-- Create an index on userId and cigarId for faster lookups
CREATE INDEX IF NOT EXISTS idx_cigar_favorites_userId ON favorites (userId);
CREATE INDEX IF NOT EXISTS idx_cigar_favorites_cigarId ON favorites (cigarId);