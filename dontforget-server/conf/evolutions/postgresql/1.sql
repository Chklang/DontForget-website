# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table T_TASK_CATEGORY (
  id                        integer not null,
  idUser                    integer,
  name                      varchar(255),
  lastUpdate                bigint not null,
  constraint uq_T_TASK_CATEGORY_name unique (name),
  constraint uq_T_TASK_CATEGORY_1 unique (idUser,name),
  constraint pk_T_TASK_CATEGORY primary key (id))
;

create table T_PLACE (
  id                        integer not null,
  idUser                    integer,
  name                      varchar(255),
  lastUpdate                bigint not null,
  constraint uq_T_PLACE_1 unique (idUser,name),
  constraint pk_T_PLACE primary key (id))
;

create table T_TAG (
  id                        integer not null,
  idUser                    integer,
  name                      varchar(255),
  lastUpdate                bigint not null,
  constraint uq_T_TAG_1 unique (idUser,name),
  constraint pk_T_TAG primary key (id))
;

create table T_TASK (
  idTask                    integer not null,
  idUser                    integer,
  text                      varchar(5000) not null,
  category_id               integer,
  status                    varchar(8) not null,
  lastUpdate                bigint not null,
  constraint ck_T_TASK_status check (status in ('OPENED','FINISHED','DELETED')),
  constraint pk_T_TASK primary key (idTask))
;

create table T_USER (
  idUser                    integer not null,
  pseudo                    varchar(32) not null,
  password                  varchar(40) not null,
  dateInscription           bigint not null,
  lastUpdate                bigint not null,
  constraint pk_T_USER primary key (idUser))
;


create table T_TASK_TAG (
  T_TASK_idTask                  integer not null,
  T_TAG_id                       integer not null,
  constraint pk_T_TASK_TAG primary key (T_TASK_idTask, T_TAG_id))
;

create table T_TASK_PLACE (
  T_TASK_idTask                  integer not null,
  T_PLACE_id                     integer not null,
  constraint pk_T_TASK_PLACE primary key (T_TASK_idTask, T_PLACE_id))
;
create sequence T_TASK_CATEGORY_seq;

create sequence T_PLACE_seq;

create sequence T_TAG_seq;

create sequence T_TASK_seq;

create sequence T_USER_seq;

alter table T_TASK_CATEGORY add constraint fk_T_TASK_CATEGORY_user_1 foreign key (idUser) references T_USER (idUser);
create index ix_T_TASK_CATEGORY_user_1 on T_TASK_CATEGORY (idUser);
alter table T_PLACE add constraint fk_T_PLACE_user_2 foreign key (idUser) references T_USER (idUser);
create index ix_T_PLACE_user_2 on T_PLACE (idUser);
alter table T_TAG add constraint fk_T_TAG_user_3 foreign key (idUser) references T_USER (idUser);
create index ix_T_TAG_user_3 on T_TAG (idUser);
alter table T_TASK add constraint fk_T_TASK_user_4 foreign key (idUser) references T_USER (idUser);
create index ix_T_TASK_user_4 on T_TASK (idUser);
alter table T_TASK add constraint fk_T_TASK_category_5 foreign key (category_id) references T_TASK_CATEGORY (id);
create index ix_T_TASK_category_5 on T_TASK (category_id);



alter table T_TASK_TAG add constraint fk_T_TASK_TAG_T_TASK_01 foreign key (T_TASK_idTask) references T_TASK (idTask);

alter table T_TASK_TAG add constraint fk_T_TASK_TAG_T_TAG_02 foreign key (T_TAG_id) references T_TAG (id);

alter table T_TASK_PLACE add constraint fk_T_TASK_PLACE_T_TASK_01 foreign key (T_TASK_idTask) references T_TASK (idTask);

alter table T_TASK_PLACE add constraint fk_T_TASK_PLACE_T_PLACE_02 foreign key (T_PLACE_id) references T_PLACE (id);

# --- !Downs

drop table if exists T_TASK_CATEGORY cascade;

drop table if exists T_PLACE cascade;

drop table if exists T_TAG cascade;

drop table if exists T_TASK cascade;

drop table if exists T_TASK_TAG cascade;

drop table if exists T_TASK_PLACE cascade;

drop table if exists T_USER cascade;

drop sequence if exists T_TASK_CATEGORY_seq;

drop sequence if exists T_PLACE_seq;

drop sequence if exists T_TAG_seq;

drop sequence if exists T_TASK_seq;

drop sequence if exists T_USER_seq;

