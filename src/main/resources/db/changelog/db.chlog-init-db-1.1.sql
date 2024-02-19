--liquibase formatted sql

--changeset nlyashkevich:1
ALTER TABLE link
ADD encrypted_key VARCHAR(255);



