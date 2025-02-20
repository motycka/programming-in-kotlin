create table if not exists greeting
(
    id bigint auto_increment primary key,
    locale text not null,
    message_key text not null,
    message_value text not null
);
