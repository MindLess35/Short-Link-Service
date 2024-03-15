--liquibase formatted sql

--changeset Nikita Lyashkevich:1
ALTER TABLE link
ADD encrypted_key VARCHAR(255);
--rollback
--ALTER TABLE link
--DROP COLUMN encrypted_key;