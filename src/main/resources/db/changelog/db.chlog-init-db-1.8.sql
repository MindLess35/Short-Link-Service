--liquibase formatted sql

--changeset nlyashkevich:1
ALTER TABLE link
RENAME COLUMN encrypted_key TO key;



