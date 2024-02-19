--liquibase formatted sql

--changeset nlyashkevich:1
ALTER TABLE token
DROP CONSTRAINT token_token_key;



