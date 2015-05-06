# --- !Ups

ALTER TABLE T_USER ADD COLUMN mail varchar(255) NOT NULL DEFAULT '' ;

# --- !Downs

ALTER TABLE T_USER DROP COLUMN mail;