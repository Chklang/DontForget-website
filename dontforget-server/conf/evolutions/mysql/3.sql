# --- !Ups

ALTER TABLE T_USER ADD mail VARCHAR( 255 ) NOT NULL DEFAULT  '';

# --- !Downs

ALTER TABLE T_USER DROP mail;