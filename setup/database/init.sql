# url path control
CREATE TABLE PATH_CTRL(
                        PATH_CTRL_ID    int                                 not null primary key auto_increment,
                        ANT_PATH        varchar(200)                        not null,
                        STATUS          varchar(2)                          null,
                        CREATEDATE      timestamp default CURRENT_TIMESTAMP not null,
                        CREATEBY        int                                 not null,
                        MODIFYDATE      timestamp default CURRENT_TIMESTAMP not null,
                        MODIFYBY        int                                 not null,
                        REMARKS         varchar(250)
);


# config
CREATE TABLE CONFIG_PARAM(
                           CONFIG_GROUP        varchar(40)                         not null,
                           CONFIG_KEY          varchar(40)                         not null,
                           CONFIG_VALUE        varchar(400),
                           CONFIG_VALUE_TYPE   varchar(20)   default 'String',

                           CREATEDATE          timestamp default CURRENT_TIMESTAMP not null,
                           CREATEBY            int                                 not null,
                           MODIFYDATE          timestamp default CURRENT_TIMESTAMP not null,
                           MODIFYBY            int                                 not null
);
CREATE UNIQUE INDEX IDX_CONFIG_PARAM_1 ON CONFIG_PARAM (CONFIG_GROUP,CONFIG_KEY);
CREATE INDEX IDX_CONFIG_PARAM_2 ON CONFIG_PARAM (CONFIG_GROUP);
CREATE INDEX IDX_CONFIG_PARAM_3 ON CONFIG_PARAM (CONFIG_KEY);


# audit
CREATE TABLE AUDIT_TRAIL(
                          AUDIT_ID        int                                 not null primary key auto_increment,
                          USER_ID         int                                 not null,
                          ACTION          varchar(20)                         not null,
                          DETAIL          varchar(200),
                          CREATEDATE      timestamp default CURRENT_TIMESTAMP not null
);
CREATE INDEX IDX_AUDIT_TRAIL_1 ON AUDIT_TRAIL (USER_ID);
CREATE INDEX IDX_AUDIT_TRAIL_2 ON AUDIT_TRAIL (ACTION);
CREATE INDEX IDX_AUDIT_TRAIL_3 ON AUDIT_TRAIL (CREATEDATE);


# cronjob
CREATE TABLE CRON_JOB(
                          JOB_ID              int                                 not null primary key auto_increment,
                          JOB_GROUP           varchar(20)                         not null,
                          JOB_NAME            varchar(80)                         not null,
                          JOB_CLASS           varchar(100)                        not null,
                          CRON_EXP            varchar(100)                        not null,

                          STATUS	            varchar(2)                          not null,
                          MANDATORY           varchar(1)                          not null,

                          CREATEDATE          timestamp default CURRENT_TIMESTAMP not null,
                          CREATEBY            int                                 not null,
                          MODIFYDATE          timestamp default CURRENT_TIMESTAMP not null,
                          MODIFYBY            int                                 not null,
                          REMARKS             varchar(250)
);
CREATE UNIQUE INDEX IDX_CRON_JOB_1 ON CRON_JOB (JOB_GROUP, JOB_NAME);

CREATE TABLE CRON_JOB_LOG(
                          SERVER_HOSTNAME     varchar(50),

                          JOB_GROUP           varchar(20)                         not null,
                          JOB_NAME            varchar(80)                         not null,
                          JOB_CLASS           varchar(100)                        not null,

                          ACTION              varchar(10),
                          SERVER_IP           varchar(50),

                          CREATEDATE          timestamp default CURRENT_TIMESTAMP not null,
                          CREATEBY            int                                 not null,
                          MODIFYDATE          timestamp default CURRENT_TIMESTAMP not null
);
CREATE INDEX IDX_CRON_JOB_LOG_1 ON CRON_JOB_LOG (SERVER_IP);
CREATE INDEX IDX_CRON_JOB_LOG_2 ON CRON_JOB_LOG (JOB_CLASS);
CREATE INDEX IDX_CRON_JOB_LOG_3 ON CRON_JOB_LOG (CREATEDATE);


# User
CREATE TABLE NOC_USER (
  USER_ID               int                                           not null primary key auto_increment,
  NAME                  varchar(100),
  STATUS                varchar(2),

  MOBILE                varchar(15),
  EMAIL                 varchar(50)                                   not null,

  COMPANY_ID            int                                           not null,
  STAFF_ID              varchar(30),
  LDAP_DOMAIN           varchar(50),

  PASSWORD              varchar(100),
  PASSWORD_MODIFYDATE   timestamp,
  LOGIN_TRIED           int default 0,

  CREATEDATE            timestamp     default CURRENT_TIMESTAMP       not null,
  CREATEBY              int                                           not null,
  MODIFYDATE            timestamp     default CURRENT_TIMESTAMP       not null,
  MODIFYBY              int                                           not null,
  REMARKS               varchar(250)
);
CREATE UNIQUE INDEX IDX_NOC_USER_1 ON NOC_USER (EMAIL);
CREATE INDEX IDX_NOC_USER_2 ON NOC_USER (COMPANY_ID);


CREATE TABLE NOC_USER_PWD_HIST(
  USER_ID               int                                   not null,
  PASSWORD              varchar(100)                          not null,
  CREATEDATE            timestamp default CURRENT_TIMESTAMP   not null
);

CREATE TABLE NOC_USER_GROUP(
  GROUP_ID              varchar(8)            not null primary key,
  GROUP_NAME            varchar(50)           not null,
  PARENT_GROUP          varchar(8),
  DESCRIPTION           varchar(250)
);

CREATE TABLE NOC_USER_USER_GROUP(
  USER_ID               int                                 not null,
  GROUP_ID              varchar(8)                          not null,
  CREATEDATE            timestamp default CURRENT_TIMESTAMP not null,
  CREATEBY              int                                 not null,
  REMARKS               varchar(250)
);
CREATE INDEX IDX_NOC_USER_USER_GROUP_1 ON NOC_USER_USER_GROUP (USER_ID);
CREATE INDEX IDX_NOC_USER_USER_GROUP_2 ON NOC_USER_USER_GROUP (GROUP_ID);
CREATE UNIQUE INDEX IDX_NOC_USER_USER_GROUP_3 ON NOC_USER_USER_GROUP (USER_ID,GROUP_ID);

CREATE TABLE NOC_COMPANY (
  COMPANY_ID            int                                 not null primary key auto_increment,
  NAME                  varchar(100),
  STATUS                varchar(2),

  CREATEDATE            timestamp default CURRENT_TIMESTAMP not null,
  CREATEBY              int                                 not null,
  MODIFYDATE            timestamp default CURRENT_TIMESTAMP not null,
  MODIFYBY              int                                 not null,
  REMARKS               varchar(250)
);
CREATE UNIQUE INDEX IDX_NOC_COMPANY_1 ON NOC_COMPANY (NAME);


CREATE TABLE NOC_USER_GROUP_PATH_CTRL(
  GROUP_ID        varchar(8)                          not null,
  PATH_CTRL_ID    int                                 not null,
  CREATEDATE      timestamp default CURRENT_TIMESTAMP not null,
  CREATEBY        int                                 not null,
  REMARKS         varchar(250)
);
CREATE UNIQUE INDEX IDX_NOC_USER_GROUP_PATH_CTRL_1 ON NOC_USER_GROUP_PATH_CTRL (GROUP_ID,PATH_CTRL_ID);
CREATE INDEX IDX_NOC_USER_GROUP_PATH_CTRL_2 ON NOC_USER_GROUP_PATH_CTRL (GROUP_ID);
CREATE INDEX IDX_NOC_USER_GROUP_PATH_CTRL_3 ON NOC_USER_GROUP_PATH_CTRL (PATH_CTRL_ID);


# otp
CREATE TABLE NOC_OTP(
  USER_ID             int                                 not null,
  ACTION              varchar(20)                         not null,
  OTP                 varchar(40)                         not null primary key,
  EXPIRYDATE          timestamp                           not null,

  CREATEDATE          timestamp default CURRENT_TIMESTAMP not null,
  CREATEBY            int                                 not null
);
CREATE INDEX IDX_NOC_OTP_1 ON NOC_OTP (USER_ID);
CREATE INDEX IDX_NOC_OTP_2 ON NOC_OTP (ACTION);


# access request
CREATE TABLE NOC_ACCESS_REQUEST(
  ACCESS_REQUEST_ID   int                                 not null primary key auto_increment,
  STATUS              varchar(2),

  REQUESTER_ID        int                                 not null,
  REQUESTER_NAME      varchar(100),
  COMPANY_ID          int                                 not null,
  COMPANY_NAME        varchar(100),
  MOBILE              varchar(15),
  EMAIL               varchar(50),

  VISIT_REASON        varchar(250),
  VISIT_LOCATION      varchar(20),
  VISIT_RACK_NUM      varchar(30),
  VISIT_DATE_FROM     timestamp,
  VISIT_DATE_TO       timestamp,

  CREATEDATE          timestamp default CURRENT_TIMESTAMP not null,
  CREATEBY            int                                 not null,
  MODIFYDATE          timestamp default CURRENT_TIMESTAMP not null,
  MODIFYBY            int                                 not null,
  REMARKS             varchar(250)
);
CREATE INDEX IDX_NOC_ACCESS_REQUEST_1 ON NOC_ACCESS_REQUEST (REQUESTER_ID);

CREATE TABLE NOC_ACCESS_REQUEST_VISITOR(
  VISITOR_ACCESS_ID   int                                 not null primary key auto_increment,
  ACCESS_REQUEST_ID   int                                 not null,

  VISITOR_NAME        varchar(100),
  COMPANY_NAME        varchar(100),
  STAFF_ID            varchar(30),
  MOBILE              varchar(15),

  TIME_IN             timestamp,
  TIME_OUT            timestamp,
  VISITOR_CARD_NUM    varchar(50),

  CREATEDATE          timestamp default CURRENT_TIMESTAMP not null,
  CREATEBY            int                                 not null,
  MODIFYDATE          timestamp default CURRENT_TIMESTAMP not null,
  MODIFYBY            int                                 not null,
  REMARKS             varchar(250)
);
CREATE INDEX IDX_NOC_ACCESS_REQUEST_VISITOR_1 ON NOC_ACCESS_REQUEST_VISITOR (ACCESS_REQUEST_ID);

CREATE TABLE NOC_ACCESS_REQUEST_EQUIP(
  EQUIP_ACCESS_ID     int                                 not null primary key auto_increment,
  ACCESS_REQUEST_ID   int                                 not null,

  EQUIP_BRAND         varchar(100),
  EQUIP_TYPE          varchar(100),
  EQUIP_MODEL         varchar(100),
  EQUIP_SERIAL        varchar(100),

  EQUIP_RACK_NUM      varchar(30),
  EQUIP_U_NUM         varchar(30),
  ACTION              varchar(10),

  CREATEDATE          timestamp default CURRENT_TIMESTAMP not null,
  CREATEBY            int                                 not null,
  MODIFYDATE          timestamp default CURRENT_TIMESTAMP not null,
  MODIFYBY            int                                 not null,
  REMARKS             varchar(250)
);
CREATE INDEX IDX_NOC_ACCESS_REQUEST_EQUIP_1 ON NOC_ACCESS_REQUEST_EQUIP (ACCESS_REQUEST_ID);

CREATE TABLE NOC_ACCESS_REQUEST_HASH(
  ACCESS_REQUEST_ID int                                 not null,
  HASHED_ID         int                                 not null,
  MODIFYDATE        timestamp default CURRENT_TIMESTAMP not null
);
CREATE UNIQUE INDEX IDX_NOC_ACCESS_REQUEST_HASH_1 ON NOC_ACCESS_REQUEST_HASH (ACCESS_REQUEST_ID);
CREATE UNIQUE INDEX IDX_NOC_ACCESS_REQUEST_HASH_2 ON NOC_ACCESS_REQUEST_HASH (HASHED_ID);


# log
CREATE TABLE NOC_OPERATION_HIST(
                                 LOG_ID          int                                 not null primary key auto_increment,
                                 ITEM_TYPE       varchar(40)                         not null,
                                 ITEM_ID         varchar(50)                         not null,
                                 DETAIL          varchar(200),
                                 USER_ID         int,
                                 CREATEDATE      timestamp default CURRENT_TIMESTAMP not null
);
CREATE INDEX IDX_NOC_OPERATION_HIST_1 ON NOC_OPERATION_HIST (ITEM_TYPE);
CREATE INDEX IDX_NOC_OPERATION_HIST_2 ON NOC_OPERATION_HIST (ITEM_ID);
CREATE INDEX IDX_NOC_OPERATION_HIST_3 ON NOC_OPERATION_HIST (CREATEDATE);
CREATE INDEX IDX_NOC_OPERATION_HIST_4 ON NOC_OPERATION_HIST (USER_ID);