--liquibase formatted sql

--changeset nlyashkevich:1
ALTER TABLE link_statistics
ALTER COLUMN date_of_last_uses
DROP NOT NULL



