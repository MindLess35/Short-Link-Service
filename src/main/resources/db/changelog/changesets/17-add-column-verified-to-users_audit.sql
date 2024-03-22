--liquibase formatted sql

--changeset Nikita Lyashkevich:1
ALTER TABLE users_audit
ADD verified BOOLEAN;
--rollback
--ALTER TABLE users_audit
--DROP COLUMN verified;
