-- Database
CREATE DATABASE rescueme;
\c rescueme;

CREATE TABLE shelter
(
    id       uuid              NOT NULL,
    PRIMARY KEY (id),
    name     character varying NOT NULL,
    province character varying NOT NULL
);

CREATE TABLE dog
(
    id          uuid              NOT NULL,
    PRIMARY KEY (id),
    name        character varying NOT NULL,
    breed       character varying NOT NULL,
    description character varying NOT NULL,
    shelter_id  uuid              NOT NULL
);

CREATE TABLE dog_details
(
    id            uuid              NOT NULL,
    name          character varying NOT NULL,
    breed         character varying NOT NULL,
    description   character varying NOT NULL,
    gender        character varying NOT NULL,
    size          character varying NOT NULL,
    color         character varying NOT NULL,
    date_of_birth character varying,
    since         DATE              NOT NULL DEFAULT CURRENT_DATE,
    PRIMARY KEY (id),
    CONSTRAINT dog_id_fk
        FOREIGN KEY (id)
            REFERENCES dog (id)
            ON DELETE CASCADE
);

CREATE INDEX dog_by_shelter_id ON dog (shelter_id);

ALTER TABLE shelter REPLICA IDENTITY FULL;
ALTER TABLE dog REPLICA IDENTITY FULL;
ALTER TABLE dog_details REPLICA IDENTITY FULL;
COMMIT;