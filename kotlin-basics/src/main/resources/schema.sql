create table if not exists i18n (
    locale text not null,
    message_key text not null,
    message_value text not null,
    primary key (locale, message_key)
);
