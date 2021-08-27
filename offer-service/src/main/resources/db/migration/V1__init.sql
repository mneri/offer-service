create table authority (
    id binary not null,
    name varchar(255) not null,
    enabled boolean,
    primary key (id));

create table offer (
    id binary not null,
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
    user binary not null,
    authority binary not null,
    primary key (user, authority));

create table user (
    id binary not null,
    username varchar(24),
    password varchar(255),
    enabled boolean,
    primary key (id));

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
    'b4d9a2724685264791d6185b65e60bdf',
    'admin',
    '$2a$10$UgHDJ0NezWztW8YCByCvnOTw/xYQ/1XHrx/6L3LvqLDU0AULOEFpi',
    true);

insert into authority (id, name, enabled) values ('3052ad78cd406e4e98480222e212b6dd', 'offer:write', true);
insert into authority (id, name, enabled) values ('f779512a591e1e4c91d7c7af352e56e6', 'offer:delete', true);
insert into authority (id, name, enabled) values ('45d78c56619fed4d91547c288c4fa315', 'user:write', true);
insert into authority (id, name, enabled) values ('c4e71faa9c35a84c84bb2522f4684aa1', 'user:delete', true);
insert into authority (id, name, enabled) values ('c1eb25c6929b4641a95a4184afcd2775', 'authority:read', true);

insert into user_authority (user, authority) values
    ('b4d9a2724685264791d6185b65e60bdf', '3052ad78cd406e4e98480222e212b6dd'),
    ('b4d9a2724685264791d6185b65e60bdf', 'f779512a591e1e4c91d7c7af352e56e6'),
    ('b4d9a2724685264791d6185b65e60bdf', '45d78c56619fed4d91547c288c4fa315'),
    ('b4d9a2724685264791d6185b65e60bdf', 'c4e71faa9c35a84c84bb2522f4684aa1'),
    ('b4d9a2724685264791d6185b65e60bdf', 'c1eb25c6929b4641a95a4184afcd2775');