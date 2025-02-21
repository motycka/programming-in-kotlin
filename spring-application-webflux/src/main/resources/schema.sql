create table if not exists hello
(
    id bigint auto_increment primary key,
    locale text not null unique,
    hello text not null
);
