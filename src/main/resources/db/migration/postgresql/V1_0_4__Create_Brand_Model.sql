-- Create the 'brands' table
CREATE TABLE IF NOT EXISTS brands
(
        brandId            BIGSERIAL PRIMARY KEY,
        brandName          VARCHAR(255) UNIQUE NOT NULL,
        countryOfOrigin    VARCHAR(255),
        description        VARCHAR(255),
        website            VARCHAR(255),
        founder            VARCHAR(50),
        yearEstablished    INT
);

CREATE INDEX IF NOT EXISTS idx_brand_cigarId ON brands (brandId);
ALTER TABLE cigars ADD COLUMN brandId BIGINT REFERENCES brands(brandId);
ALTER TABLE cigars
ADD CONSTRAINT fk_cigar_brand
FOREIGN KEY (brandId) REFERENCES brands(brandId);
