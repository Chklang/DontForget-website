# --- !Ups

create table T_TOKEN (
  idUser                    integer,
  token                     varchar(36),
  deviceId                  varchar(36) not null,
  constraint pk_T_TOKEN primary key (idUser, token))
;

alter table T_TOKEN add constraint fk_T_TOKEN_user_1 foreign key (idUser) references T_USER (idUser) on delete restrict on update restrict;
create index ix_T_TOKEN_user_1 on T_TOKEN (idUser);

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists T_TOKEN;

SET REFERENTIAL_INTEGRITY TRUE;
