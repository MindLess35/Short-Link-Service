--liquibase formatted sql

--changeset Nikita Lyashkevich:1
ALTER TABLE link_statistics
RENAME COLUMN life_time TO time_to_live;
--rollback
--ALTER TABLE link_statistics
--RENAME COLUMN time_to_live TO life_time;



