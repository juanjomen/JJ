-- Tables for PostgreSQL database implemenation
-- drop table if exists RESULT_VALUE;
-- drop table if exists RESULT;
-- drop table if exists REPORT;
-- drop table if exists TRUTH_VALUE;
-- drop table if exists TRUTH;
-- drop table if exists TRUTH_SET;
-- drop sequence if exists TRUTH_SET_SEQ;
-- drop sequence if exists TRUTH_SEQ;
-- drop sequence if exists TRUTH_VALUE_SEQ;
-- drop sequence if exists REPORT_SEQ;
-- drop sequence if exists RESULT_SEQ;
-- drop sequence if exists RESULT_VALUE_SEQ;

create sequence TRUTH_SET_SEQ;
create table TRUTH_SET(
  ID bigint not null,
  TYPE varchar(16) not null,
  NAME varchar(255) not null,
  VERSION varchar(255) not null
);
alter table TRUTH_SET add constraint PK_TRUTH_SET primary key (ID);
alter table TRUTH_SET alter column ID set default nextval('TRUTH_SET_SEQ');

create sequence TRUTH_SEQ;
create table TRUTH(
  ID bigint not null,
  SET_ID bigint not null,
  VAL varchar(255) not null,
  CONTEXT_VAL varchar(255)
);
alter table TRUTH add constraint PK_TRUTH primary key (ID);
alter table TRUTH alter column ID set default nextval('TRUTH_SEQ');
create index IDX1_TRUTH on TRUTH(SET_ID);
alter table TRUTH add constraint FK1_TRUTH foreign key (SET_ID) references TRUTH_SET(ID);

create sequence TRUTH_VALUE_SEQ;
create table TRUTH_VALUE(
  ID bigint not null,
  TRUTH_ID bigint not null,
  VAL varchar(255) not null,
  CONTEXT_VAL varchar(512),
  SCORE decimal(20, 8) not null,
  IS_POSITIVE boolean not null default TRUE
);
alter table TRUTH_VALUE add constraint PK_TRUTH_VALUE primary key (ID);
alter table TRUTH_VALUE alter column ID set default nextval('TRUTH_VALUE_SEQ');
create index IDX1_TRUTH_VALUE on TRUTH_VALUE(TRUTH_ID);
alter table TRUTH_VALUE add constraint FK1_TRUTH_VALUE foreign key (TRUTH_ID) references TRUTH(ID);

create sequence REPORT_SEQ;
create table REPORT(
  ID bigint not null,
  SET_ID bigint not null,
  ALGORITHM varchar(255) not null,
  VERSION varchar(255) not null,
  COMPARE_METHOD varchar(255) not null,
  SCORING_METHOD varchar(255) not null,
  DESCRIPTION varchar(512),
  RUN_DATE timestamp not null
);
alter table REPORT add constraint PK_REPORT primary key (ID);
alter table REPORT alter column ID set default nextval('REPORT_SEQ');
create index IDX1_REPORT on REPORT(SET_ID);
alter table REPORT add constraint FK1_REPORT foreign key (SET_ID) references TRUTH_SET(ID);

create sequence RESULT_SEQ;
create table RESULT(
  ID bigint not null,
  REPORT_ID bigint not null,
  TRUTH_ID bigint not null,
  DURATION decimal(20, 12) not null,
  ACCURACY decimal(20, 8) not null,
  TRUE_POS_CNT bigint not null DEFAULT 0,
  TRUE_NEG_CNT bigint not null DEFAULT 0,
  FALSE_POS_CNT bigint not null DEFAULT 0,
  FALSE_NEG_CNT bigint not null DEFAULT 0
);
alter table RESULT add constraint PK_RESULT primary key (ID);
alter table RESULT alter column ID set default nextval('RESULT_SEQ');
create index IDX1_RESULT on RESULT(REPORT_ID);
create index IDX2_RESULT on RESULT(TRUTH_ID);
create unique index IDX3_RESULT on RESULT(REPORT_ID,TRUTH_ID);
alter table RESULT add constraint FK1_RESULT foreign key (REPORT_ID) references REPORT(ID);
alter table RESULT add constraint FK2_RESULT foreign key (TRUTH_ID) references TRUTH(ID);

-- stores the values for each RESULT from the test
create sequence RESULT_VALUE_SEQ;
create table RESULT_VALUE(
  ID bigint not null,
  RESULT_ID bigint not null,
  ACCURACY decimal(20, 8) not null,
  VAL varchar(255) not null,
  VAL_DETAILS varchar(512),
  TRUTH_VALUE_ID bigint not null DEFAULT 0
);
alter table RESULT_VALUE add constraint PK_RESULT_VALUE primary key (ID);
alter table RESULT_VALUE alter column ID set default nextval('RESULT_VALUE_SEQ');
create index IDX1_RESULT_VALUE on RESULT_VALUE(RESULT_ID);
alter table RESULT_VALUE add constraint FK1_RESULT_VALUE foreign key (RESULT_ID) references RESULT(ID);
