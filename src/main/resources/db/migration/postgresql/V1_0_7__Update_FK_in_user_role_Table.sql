-- Modify the foreign key constraint for userId in user_role_junction table
ALTER TABLE user_role_junction
    DROP CONSTRAINT fk_user_role_junction_user_id,
    ADD CONSTRAINT fk_user_role_junction_user_id
        FOREIGN KEY (userId)
            REFERENCES users (userId) ON DELETE CASCADE;

-- Modify the foreign key constraint for userId in tokens table
ALTER TABLE tokens
    DROP CONSTRAINT tokens_userid_fkey,
    ADD CONSTRAINT tokens_userid_fkey
        FOREIGN KEY (userId)
            REFERENCES users(userId)
            ON DELETE CASCADE;
