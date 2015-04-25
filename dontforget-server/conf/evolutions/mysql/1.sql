# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table T_TASK_CATEGORY (
  id                        integer auto_increment not null,
  idUser                    integer,
  name                      varchar(255),
  lastUpdate                bigint not null,
  constraint uq_T_TASK_CATEGORY_name unique (name),
  constraint uq_T_TASK_CATEGORY_1 unique (idUser,name),
  constraint pk_T_TASK_CATEGORY primary key (id))
;

create table T_PLACE (
  id                        integer auto_increment not null,
  idUser                    integer,
  name                      varchar(255),
  lastUpdate                bigint not null,
  constraint uq_T_PLACE_1 unique (idUser,name),
  constraint pk_T_PLACE primary key (id))
;

create table T_TAG (
  id                        integer auto_increment not null,
  idUser                    integer,
  name                      varchar(255),
  lastUpdate                bigint not null,
  constraint uq_T_TAG_1 unique (idUser,name),
  constraint pk_T_TAG primary key (id))
;

create table T_TASK (
  idTask                    integer auto_increment not null,
  idUser                    integer,
  text                      varchar(5000) not null,
  category_id               integer,
  status                    varchar(8) not null,
  lastUpdate                bigint not null,
  constraint ck_T_TASK_status check (status in ('OPENED','FINISHED','DELETED')),
  constraint pk_T_TASK primary key (idTask))
;

create table T_USER (
  idUser                    integer auto_increment not null,
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
alter table T_TASK_CATEGORY add constraint fk_T_TASK_CATEGORY_user_1 foreign key (idUser) references T_USER (idUser) on delete restrict on update restrict;
create index ix_T_TASK_CATEGORY_user_1 on T_TASK_CATEGORY (idUser);
alter table T_PLACE add constraint fk_T_PLACE_user_2 foreign key (idUser) references T_USER (idUser) on delete restrict on update restrict;
create index ix_T_PLACE_user_2 on T_PLACE (idUser);
alter table T_TAG add constraint fk_T_TAG_user_3 foreign key (idUser) references T_USER (idUser) on delete restrict on update restrict;
create index ix_T_TAG_user_3 on T_TAG (idUser);
alter table T_TASK add constraint fk_T_TASK_user_4 foreign key (idUser) references T_USER (idUser) on delete restrict on update restrict;
create index ix_T_TASK_user_4 on T_TASK (idUser);
alter table T_TASK add constraint fk_T_TASK_category_5 foreign key (category_id) references T_TASK_CATEGORY (id) on delete restrict on update restrict;
create index ix_T_TASK_category_5 on T_TASK (category_id);



alter table T_TASK_TAG add constraint fk_T_TASK_TAG_T_TASK_01 foreign key (T_TASK_idTask) references T_TASK (idTask) on delete restrict on update restrict;

alter table T_TASK_TAG add constraint fk_T_TASK_TAG_T_TAG_02 foreign key (T_TAG_id) references T_TAG (id) on delete restrict on update restrict;

alter table T_TASK_PLACE add constraint fk_T_TASK_PLACE_T_TASK_01 foreign key (T_TASK_idTask) references T_TASK (idTask) on delete restrict on update restrict;

alter table T_TASK_PLACE add constraint fk_T_TASK_PLACE_T_PLACE_02 foreign key (T_PLACE_id) references T_PLACE (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table T_TASK_CATEGORY;

drop table T_PLACE;

drop table T_TAG;

drop table T_TASK;

drop table T_TASK_TAG;

drop table T_TASK_PLACE;

drop table T_USER;

SET FOREIGN_KEY_CHECKS=1;

