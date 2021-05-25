use oms_uat;

-- auto-generated definition
create table general_terms_confirmation_audit
(
    id                int auto_increment
        primary key,
    username              varchar(255)                   null,
    public_ip         varchar(45)                        null,
    audit_type        enum ('PARTNER') default 'PARTNER' null,
    created_time      timestamp                          null,
    user_agent        varchar(255)                       null,
    created_time_unix timestamp                          null
);