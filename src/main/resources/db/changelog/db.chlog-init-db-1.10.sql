--liquibase formatted sql

--changeset nlyashkevich:1
ALTER TABLE link_statistics
ALTER COLUMN link_id
DROP NOT NULL;