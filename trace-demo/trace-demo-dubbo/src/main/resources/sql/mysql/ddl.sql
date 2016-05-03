use mysql

CREATE DATABASE IF NOT EXISTS trace DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

use trace;

drop table if exists employee;
create table employee
(
   ID int not null auto_increment,
   NAME char(100),
   DELETE_FLAG int,
   VERSION int,
   CREATE_TIME datetime,
   UPDATE_TIME datetime,
   primary key (ID)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;