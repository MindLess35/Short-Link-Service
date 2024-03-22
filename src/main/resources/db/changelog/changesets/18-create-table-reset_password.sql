--liquibase formatted sql

--changeset Nikita Lyashkevich:1
CREATE TABLE reset_password (
    id          BIGSERIAL       PRIMARY KEY,
    token       VARCHAR(36)     NOT NULL UNIQUE,
    created_at  TIMESTAMP       NOT NULL,
    reset_at    TIMESTAMP,
    user_id     BIGINT          NOT NULL REFERENCES users (id)
);
--rollback DROP TABLE reset_password;