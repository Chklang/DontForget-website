# --- !Ups

create table T_CATEGORY_TO_DELETE (
  uuidCategory              VARCHAR(47) not null,
  dateDeletion              bigint not null,
  idUser		            integer not null,
  constraint pk_T_CATEGORY_TO_DELETE primary key (uuidCategory))
;

create table T_TAG_TO_DELETE (
  uuidTag             		VARCHAR(47) not null,
  dateDeletion              bigint not null,
  idUser		            integer not null,
  constraint pk_T_TAG_TO_DELETE primary key (uuidTag))
;

create table T_PLACE_TO_DELETE (
  uuidPlace              	VARCHAR(47) not null,
  dateDeletion              bigint not null,
  idUser		            integer not null,
  constraint pk_T_PLACE_TO_DELETE primary key (uuidPlace))
;

create table T_TASK_TO_DELETE (
  uuidTask              	VARCHAR(47) not null,
  dateDeletion              bigint not null,
  idUser		            integer not null,
  constraint pk_T_TASK_TO_DELETE primary key (uuidTask))
;

alter table T_CATEGORY_TO_DELETE add constraint fk_T_CATEGORY_TO_DELETE_T_USER foreign key (idUser) references T_USER (idUser) on delete restrict on update restrict;
alter table T_TAG_TO_DELETE add constraint fk_T_TAG_TO_DELETE_T_USER foreign key (idUser) references T_USER (idUser) on delete restrict on update restrict;
alter table T_PLACE_TO_DELETE add constraint fk_T_PLACE_TO_DELETE_T_USER foreign key (idUser) references T_USER (idUser) on delete restrict on update restrict;
alter table T_TASK_TO_DELETE add constraint fk_T_TASK_TO_DELETE_T_USER foreign key (idUser) references T_USER (idUser) on delete restrict on update restrict;

ALTER TABLE T_TOKEN ADD lastUpdate bigint not null;
ALTER TABLE T_USER DROP COLUMN lastUpdate;

# --- !Downs

ALTER TABLE T_USER ADD lastUpdate bigint not null;
ALTER TABLE T_TOKEN DROP COLUMN lastUpdate;

drop table if exists T_CATEGORY_TO_DELETE;
drop table if exists T_TAG_TO_DELETE;
drop table if exists T_PLACE_TO_DELETE;
drop table if exists T_TASK_TO_DELETE;