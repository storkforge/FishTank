CREATE TABLE appUser
(
    id       INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name     TEXT                                     NOT NULL,
    password TEXT                                     NOT NULL,
    email    TEXT                                     NOT NULL,
    accessid INTEGER                                  NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);