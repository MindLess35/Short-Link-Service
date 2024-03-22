--liquibase formatted sql

--changeset Nikita Lyashkevich:1
CREATE TABLE mail_verification (
    id          BIGSERIAL       PRIMARY KEY,
    token       VARCHAR(36)     NOT NULL UNIQUE,
    created_at  TIMESTAMP       NOT NULL,
    verified_at TIMESTAMP,
    user_id     BIGINT          NOT NULL REFERENCES users (id)
);
--rollback DROP TABLE mail_verification;

