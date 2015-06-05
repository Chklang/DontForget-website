# --- !Ups

create table T_CATEGORY_TO_DELETE (
  uuidCategory              VARCHAR(47) not null,
  dateDeletion              bigint not null,
  constraint pk_T_TASK_CATEGORY primary key (uuidCategory))
;

create table T_TAG_TO_DELETE (
  uuidTag             		VARCHAR(47) not null,
  dateDeletion              bigint not null,
  constraint pk_T_TASK_TAG primary key (uuidTag))
;

create table T_PLACE_TO_DELETE (
  uuidPlace              	VARCHAR(47) not null,
  dateDeletion              bigint not null,
  constraint pk_T_TASK_PLACE primary key (uuidPlace))
;

create table T_TASK_TO_DELETE (
  uuidTask              	VARCHAR(47) not null,
  dateDeletion              bigint not null,
  constraint pk_T_TASK_TASK primary key (uuidTask))
;

# --- !Downs

drop table T_CATEGORY_TO_DELETE;
drop table T_TAG_TO_DELETE;
drop table T_PLACE_TO_DELETE;
drop table T_TASK_TO_DELETE;