CREATE TABLE IF NOT EXISTS sex
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name TEXT                                     NOT NULL,
    CONSTRAINT pk_sex PRIMARY KEY (id)
);