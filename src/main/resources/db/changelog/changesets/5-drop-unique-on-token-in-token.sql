--liquibase formatted sql

--changeset Nikita Lyashkevich:1
ALTER TABLE token
DROP CONSTRAINT token_token_key;
--rollback
--ALTER TABLE token
--ADD CONSTRAINT token_token_key
--UNIQUE(token);



