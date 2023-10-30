-- Create the 'cigars' table
CREATE TABLE IF NOT EXISTS cigars
(
    cigarId               BIGSERIAL PRIMARY KEY,
    name                  VARCHAR(255),
    wrapper               VARCHAR(255),
    binder                VARCHAR(255),
    filler                VARCHAR(255),
    cigarStrength         VARCHAR(50),
    shape                 VARCHAR(50),
    rolling               VARCHAR(50),
    agingPotential        BOOLEAN,
    harvesting            VARCHAR(255),
    curing                VARCHAR(255),
    aging                 VARCHAR(255),
    priceRange            INT,
    limitedEdition        BOOLEAN,
    historicalBackground  VARCHAR(255),
    flavors               VARCHAR(255),
    fermentation          VARCHAR(255),
    pairings              VARCHAR(255),
    humidificationStorage VARCHAR(255),
    created               TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_cigar_cigarId ON cigars (cigarId);

-- Create the 'images' table
CREATE TABLE IF NOT EXISTS images
(
    imageId  BIGSERIAL PRIMARY KEY,
    cigarId  BIGINT REFERENCES cigars (cigarId),
    link     VARCHAR(1000) NOT NULL,
    created  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    publicId VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS idx_images_imageId ON images (imageId);
CREATE INDEX IF NOT EXISTS idx_images_cigarId ON images (cigarId);
