-- Add roles if they do not exist
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM roles WHERE authority = 'USER') THEN
            INSERT INTO roles (authority, created) VALUES ('USER', current_timestamp);
        END IF;

        IF NOT EXISTS (SELECT 1 FROM roles WHERE authority = 'ADMIN') THEN
            INSERT INTO roles (authority, created) VALUES ('ADMIN', current_timestamp);
        END IF;
    END
$$;

