CREATE TABLE IF NOT EXISTS roles
(
    roleId    BIGSERIAL PRIMARY KEY,
    authority VARCHAR(125) NOT NULL,
    created   TIMESTAMP    NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_roles_roleId ON roles (roleId);
CREATE INDEX IF NOT EXISTS idx_roles_created ON roles (created);


CREATE TABLE IF NOT EXISTS users
(
    userId   BIGSERIAL PRIMARY KEY,
    username VARCHAR(125) UNIQUE NOT NULL,
    password VARCHAR(125) NOT NULL,
    email    VARCHAR(125) UNIQUE NOT NULL,
    created  TIMESTAMP    NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_users_userId ON users (userId);
CREATE INDEX IF NOT EXISTS idx_users_created ON users (created);
CREATE INDEX IF NOT EXISTS idx_users_username ON users (username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users (email);


CREATE TABLE IF NOT EXISTS user_role_junction
(
    userId BIGINT,
    roleId BIGINT,
    PRIMARY KEY (userId, roleId),
    CONSTRAINT fk_user_role_junction_user_id
        FOREIGN KEY (userId)
            REFERENCES users (userId),
    CONSTRAINT fk_user_role_junction_role_id
        FOREIGN KEY (roleId)
            REFERENCES roles (roleId)
);

CREATE INDEX IF NOT EXISTS idx_user_role_junction_userId ON user_role_junction (userId);
CREATE INDEX IF NOT EXISTS idx_user_role_junction_roleId ON user_role_junction (roleId);
