CREATE TABLE CONFIG_PARAM_HIST(
  CONFIG_GROUP      VARCHAR2(40)            not null,
  CONFIG_KEY        VARCHAR2(40)            not null,
  CONFIG_VALUE      VARCHAR2(400),
  CONFIG_VALUE_TYPE VARCHAR2(20),
  ENCRYPT           VARCHAR2(1),
  ACTION            VARCHAR2(1)             not null,
  CREATEDATE        DATE                    default SYSDATE not null
);


CREATE TABLE CRON_JOB_HIST(
  JOB_ID        NUMBER              not null,
  JOB_GROUP     VARCHAR2(20)        not null,
  JOB_NAME      VARCHAR2(80)        not null,
  JOB_CLASS     VARCHAR2(100)       not null,
  CRON_EXP      VARCHAR2(100)       not null,
  STATUS        VARCHAR2(2)         not null,
  MANDATORY     VARCHAR2(1)         not null,
  ACTION        VARCHAR2(1)         not null,
  CREATEDATE    DATE                default SYSDATE not null
);


CREATE TABLE PATH_CTRL_HIST(
    PATH_CTRL_ID    number                  not null,
    PATH            varchar2(200)           not null,
    STATUS          varchar2(2)             null,
    DESCRIPTION     varchar2(50)            not null,
    ACTION          VARCHAR2(1)             not null,
    CREATEDATE      DATE                    default SYSDATE not null
);


CREATE TABLE USER_ROLE_HIST(
  ROLE_ID               varchar2(20),
  ROLE_DESC             varchar2(50)                    not null,
  PARENT_ROLE_ID        varchar2(20),
  STATUS                varchar2(2),
  ACTION                VARCHAR2(1)                     not null,
  CREATEDATE            DATE                            default SYSDATE not null
);


CREATE TABLE USER_ROLE_PATH_CTRL_HIST(
    ROLE_ID         varchar2(20)                            not null,
    PATH_CTRL_ID    number                                  not null,
    ACTION          VARCHAR2(1)                             not null,
    CREATEDATE      DATE                                    default SYSDATE not null
);

CREATE TABLE USER_USER_ROLE_HIST(
    USER_ID               varchar2(10)                not null,
    ROLE_ID               varchar2(20)                not null,
    ACTION                VARCHAR2(1)                 not null,
    CREATEDATE            DATE                        default SYSDATE not null
);