-- Database
CREATE DATABASE rescueme;
\c rescueme;

CREATE TABLE shelter
(
    id       serial            NOT NULL,
    PRIMARY KEY (id),
    name     character varying NOT NULL,
    province character varying NOT NULL
);

CREATE TABLE dog
(
    id          serial              NOT NULL,
    PRIMARY KEY (id),
    name     character varying NOT NULL,
    breed       character varying NOT NULL,
    description character varying NOT NULL
);

INSERT INTO shelter (name, province)
VALUES ('Casa de acogida Madrid', 'Madrid'),
       ('Casa de acogida galicia', 'Galicia'),
       ('Casa de acogida Guadalajara', 'Guadalajara'),
       ('Casa de acogida Toledo', 'Toledo');

ALTER TABLE shelter REPLICA IDENTITY FULL;
COMMIT;