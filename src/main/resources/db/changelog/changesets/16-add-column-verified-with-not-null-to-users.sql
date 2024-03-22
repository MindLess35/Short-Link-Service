--liquibase formatted sql

--changeset Nikita Lyashkevich:1
ALTER TABLE users
ADD verified BOOLEAN;
--rollback
--ALTER TABLE users
--DROP COLUMN verified;

--changeset Nikita Lyashkevich:2
ALTER TABLE users
ALTER COLUMN verified
SET NOT NULL;
--rollback
-- ALTER TABLE users
-- ALTER COLUMN verified
-- DROP NOT NULL;

