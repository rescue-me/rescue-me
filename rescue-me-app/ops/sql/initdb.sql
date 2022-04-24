-- Database
CREATE DATABASE rescueme;
\c rescueme;

CREATE TABLE shelters
(
    id       serial            NOT NULL,
    PRIMARY KEY (id),
    name     character varying NOT NULL,
    province character varying NOT NULL
);

INSERT INTO shelters (name, province)
VALUES ('Casa de acogida Madrid', 'Madrid'),
       ('Casa de acogida galicia', 'Galicia'),
       ('Casa de acogida Guadalajara', 'Guadalajara'),
       ('Casa de acogida Toledo', 'Toledo');

ALTER TABLE shelters REPLICA IDENTITY FULL;
COMMIT;