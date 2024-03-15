--liquibase formatted sql

--changeset Nikita Lyashkevich:1
ALTER TABLE link
RENAME COLUMN encrypted_key TO key;
--rollback
--ALTER TABLE link
--RENAME COLUMN key TO encrypted_key;



