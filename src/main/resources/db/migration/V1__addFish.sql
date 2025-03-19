create table fish
(
    id          serial
        constraint fish_pk
            primary key,
    name        varchar not null,
    species     varchar not null,
    userid      integer not null,
    description varchar not null,
    watertypeid integer not null,
    sexid       integer not null
);