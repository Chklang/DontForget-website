# --- !Ups

ALTER TABLE T_USER ADD codelang VARCHAR(30);

# --- !Downs

ALTER TABLE T_USER DROP COLUMN codelang;
