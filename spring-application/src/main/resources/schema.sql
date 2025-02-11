drop table if exists exercise;
drop table if exists activity;
drop table if exists users;

create table if not exists users
(
    id bigint auto_increment primary key,
    name text not null,
    role text not null
);

create table if not exists activity
(
    id bigint auto_increment primary key,
    user_id bigserial references users(id) on delete cascade,
    name text not null,
    kcal_per_minute decimal not null,
    unique (user_id, name)
);

create table if not exists exercise
(
    id bigint auto_increment primary key,
    user_id bigint not null references users(id) on delete cascade,
    activity_id bigint not null references activity(id),
    start_time timestamp not null,
    duration bigint not null
);

drop table if exists character_attributes;
drop table if exists character;

create table if not exists character
(
    id pg_catalog.uuid primary key,
    user_id bigint not null references users(id) on delete cascade,
    name text not null,
    class text not null
);

create table if not exists character_attributes
(
    character_id pg_catalog.uuid not null references character(id) on delete cascade,
    level bigint not null,
    experience bigint not null,
    health bigint not null,
    attack bigint not null,
    speed bigint not null,
    stamina bigint not null,
    mana text not null
);

create table match
(
    id bigint pg_catalog.uuid primary key,
    rounds bigint not null
);

create table match_character
(
    match_id bigint not null references match(id) on delete cascade,
    character_id pg_catalog.uuid not null references character(id) on delete cascade,
    health_lost bigint not null,
    is_winner boolean not null
);
