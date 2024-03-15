--liquibase formatted sql

--changeset Nikita Lyashkevich:1
ALTER TABLE link_statistics
ALTER COLUMN date_of_last_uses
DROP NOT NULL;
--rollback
--ALTER TABLE link_statistics
--ALTER COLUMN date_of_last_uses
--SET NOT NULL;




