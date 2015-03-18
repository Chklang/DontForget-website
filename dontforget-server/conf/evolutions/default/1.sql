# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table T_PLACE (
  id_place                  integer not null,
  idUser                    integer,
  place                     varchar(255),
  constraint uq_T_PLACE_1 unique (idUser,place),
  constraint pk_T_PLACE primary key (id_place))
;

create table T_TAG (
  id_tag                    integer not null,
  idUser                    integer,
  tag                       varchar(255),
  constraint uq_T_TAG_1 unique (idUser,tag),
  constraint pk_T_TAG primary key (id_tag))
;

create table T_TASK (
  idTask                    integer not null,
  idUser                    integer,
  text                      varchar(5000) not null,
  taskStatus                varchar(8) not null,
  constraint ck_T_TASK_taskStatus check (taskStatus in ('OPENED','FINISHED','DELETED')),
  constraint pk_T_TASK primary key (idTask))
;

create table T_USER (
  idUser                    integer not null,
  pseudo                    varchar(32) not null,
  password                  varchar(40) not null,
  dateInscription           bigint not null,
  constraint pk_T_USER primary key (idUser))
;


create table T_TASK_TAG (
  T_TASK_idTask                  integer not null,
  T_TAG_id_tag                   integer not null,
  constraint pk_T_TASK_TAG primary key (T_TASK_idTask, T_TAG_id_tag))
;

create table T_TASK_PLACE (
  T_TASK_idTask                  integer not null,
  T_PLACE_id_place               integer not null,
  constraint pk_T_TASK_PLACE primary key (T_TASK_idTask, T_PLACE_id_place))
;
create sequence T_PLACE_seq;

create sequence T_TAG_seq;

create sequence T_TASK_seq;

create sequence T_USER_seq;

alter table T_PLACE add constraint fk_T_PLACE_user_1 foreign key (idUser) references T_USER (idUser) on delete restrict on update restrict;
create index ix_T_PLACE_user_1 on T_PLACE (idUser);
alter table T_TAG add constraint fk_T_TAG_user_2 foreign key (idUser) references T_USER (idUser) on delete restrict on update restrict;
create index ix_T_TAG_user_2 on T_TAG (idUser);
alter table T_TASK add constraint fk_T_TASK_user_3 foreign key (idUser) references T_USER (idUser) on delete restrict on update restrict;
create index ix_T_TASK_user_3 on T_TASK (idUser);



alter table T_TASK_TAG add constraint fk_T_TASK_TAG_T_TASK_01 foreign key (T_TASK_idTask) references T_TASK (idTask) on delete restrict on update restrict;

alter table T_TASK_TAG add constraint fk_T_TASK_TAG_T_TAG_02 foreign key (T_TAG_id_tag) references T_TAG (id_tag) on delete restrict on update restrict;

alter table T_TASK_PLACE add constraint fk_T_TASK_PLACE_T_TASK_01 foreign key (T_TASK_idTask) references T_TASK (idTask) on delete restrict on update restrict;

alter table T_TASK_PLACE add constraint fk_T_TASK_PLACE_T_PLACE_02 foreign key (T_PLACE_id_place) references T_PLACE (id_place) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists T_PLACE;

drop table if exists T_TAG;

drop table if exists T_TASK;

drop table if exists T_TASK_TAG;

drop table if exists T_TASK_PLACE;

drop table if exists T_USER;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists T_PLACE_seq;

drop sequence if exists T_TAG_seq;

drop sequence if exists T_TASK_seq;

drop sequence if exists T_USER_seq;

