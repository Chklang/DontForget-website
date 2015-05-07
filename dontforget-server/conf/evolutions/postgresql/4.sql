# --- !Ups

ALTER TABLE T_USER ADD COLUMN codelang varchar(30);

# --- !Downs

ALTER TABLE T_USER DROP COLUMN codelang;