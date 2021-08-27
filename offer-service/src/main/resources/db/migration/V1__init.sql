create table authority (
    id varchar(255) not null,
    name varchar(255) not null,
    enabled boolean,
    primary key (id));

create table offer (
    id varchar(255) not null,
    title varchar(256),
    description varchar(8192),
    publisher varchar(255) not null,
    price decimal(16,2) not null,
    currency varchar(255),
    cancelled boolean,
    create_time timestamp not null,
    end_time timestamp not null,
    primary key (id));

create table user_authority (
    user varchar(255) not null,
    authority varchar(255) not null,
    primary key (user, authority));

create table user (
    id varchar(255) not null,
    username varchar(24),
    password varchar(255),
    enabled boolean,
    primary key (id));

create table user_role (
    user varchar(255) not null,
    role varchar(255) not null,
    primary key (user, role));

alter table authority
    add constraint idx_authority_name_unq unique (name);
alter table offer
    add constraint fk_offer_publisher foreign key (publisher) references user;
alter table user
    add constraint idx_user_username_unq unique (username);
alter table user_authority
    add constraint fk_user_authority_user foreign key (user) references user;
alter table user_authority
    add constraint fk_user_authority_authority foreign key (authority) references authority;

insert into user (id, username, password, enabled) values (
    '72a2d9b4-8546-4726-91d6-185b65e60bdf',
    'admin',
    '$2a$10$UgHDJ0NezWztW8YCByCvnOTw/xYQ/1XHrx/6L3LvqLDU0AULOEFpi',
    true);

insert into authority (id, name, enabled) values ('78ad5230-40cd-4e6e-9848-0222e212b6dd', 'offer:write', true);
insert into authority (id, name, enabled) values ('2a5179f7-1e59-4c1e-91d7-c7af352e56e6', 'offer:delete', true);
insert into authority (id, name, enabled) values ('568cd745-9f61-4ded-9154-7c288c4fa315', 'user:write', true);
insert into authority (id, name, enabled) values ('aa1fe7c4-359c-4ca8-84bb-2522f4684aa1', 'user:delete', true);
insert into authority (id, name, enabled) values ('c625ebc1-9b92-4146-a95a-4184afcd2775', 'authority:read', true);

insert into user_authority (user, authority) values
    ('72a2d9b4-8546-4726-91d6-185b65e60bdf', '78ad5230-40cd-4e6e-9848-0222e212b6dd'),
    ('72a2d9b4-8546-4726-91d6-185b65e60bdf', '2a5179f7-1e59-4c1e-91d7-c7af352e56e6'),
    ('72a2d9b4-8546-4726-91d6-185b65e60bdf', '568cd745-9f61-4ded-9154-7c288c4fa315'),
    ('72a2d9b4-8546-4726-91d6-185b65e60bdf', 'aa1fe7c4-359c-4ca8-84bb-2522f4684aa1'),
    ('72a2d9b4-8546-4726-91d6-185b65e60bdf', 'c625ebc1-9b92-4146-a95a-4184afcd2775');