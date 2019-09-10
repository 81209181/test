-- .MMMMMMMMMMMMMMMMMMMMM ..............  .      .             . ..     .  ............................
-- .M~   .... ..........M ............. . .  ..                . .   .  .. ............................
--  M~.    ..ZMM+ ......M ........ZMM+    .        ..          .       ... .,.  .........?MMD,. .......
-- .M~   .MMMMMMMMMD ...M .....MMMMMMMMM. . ..  .MMM   ..MM. . . MMMMMM,.. .=MM.......+MMMMMMMMM.......
-- .M~  :MMN ...  M~,...M ....MMM ....+MMZ  ..   MMMM? ..MM.   . MM~   ... .=MM......MMM,. . .OM  .....
--  M~. MMM    . . .. ..M .. NMM .   ..,MM       MM$MM   MM      MM:. . .  .=MM.....,MM...... . .......
--  M~. MM.... ... .....M ...MM~ .......MM~      MMI~MM+.MM      MMMMMM,.. .=MM ....OMM ...~~~~~~~ ....
--  M~. MM7.............M ...MMM.......,MM       MMI ,MM7MM      MM~    .. .=MM.....~MM....MMMMMMD.....
-- .M~. OMM=.  .  D   ..M .. :MM8.. .. MMM       MMI  ,MMMM      MM~    .. .=MM......MMM. ... ?MM......
--  M~ ..,MMMMMMMMMM ...M .....MMMMMMMMMD        MMI  .,MMM      MM~  . . ..=MM ..... MMMMMMMMMM.......
--  M~......MMMMM+  ....M .......DMMMM.          MM?.. ..MM.     MM~  . .. .=MM........ :MMMMM  .......
-- .M~...... . . .  .. .M    . ..  . ......................................................  ..........
-- .M$IIIIIIIIIIIIIIIIIIM .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM .
-- Config
CREATE TABLE CONFIG_PARAM(
    CONFIG_GROUP        varchar2(40)            not null,
    CONFIG_KEY          varchar2(40)            not null,
    CONFIG_VALUE        varchar2(400),
    CONFIG_VALUE_TYPE   varchar2(20)            default 'String',
    ENCRYPT             varchar2(1)             default 'N',

    CREATEDATE          date                    default SYSDATE not null,
    CREATEBY            varchar2(10)            not null,
    MODIFYDATE          date                    default SYSDATE not null,
    MODIFYBY            varchar2(10)            not null
);
CREATE UNIQUE INDEX IDX_CONFIG_PARAM_1 ON CONFIG_PARAM (CONFIG_GROUP,CONFIG_KEY);
CREATE INDEX IDX_CONFIG_PARAM_2 ON CONFIG_PARAM (CONFIG_GROUP);
CREATE INDEX IDX_CONFIG_PARAM_3 ON CONFIG_PARAM (CONFIG_KEY);

CREATE OR REPLACE TRIGGER TRIGGER_CONFIG_PARAM_1
    BEFORE INSERT ON CONFIG_PARAM
    FOR EACH ROW
BEGIN
    :NEW.CREATEDATE := SYSDATE;
END;

CREATE OR REPLACE TRIGGER TRIGGER_CONFIG_PARAM_2
    BEFORE UPDATE ON CONFIG_PARAM
    FOR EACH ROW
BEGIN
    :NEW.CREATEDATE := :OLD.CREATEDATE;
    :NEW.CREATEBY := :OLD.CREATEBY;
    :NEW.MODIFYDATE := SYSDATE;
END;





-- MMMMMMMMMMMMMMMMMM.................................... .......... ...... ................... .......
-- M+...... ........M................  . ... .... ... ..  .....  ..  .... . .. . ..  .  .. .  .    .
-- M+....MMMMMMM....M.. .MMMMM7 .  ..,MMMMMM?... . .MMM. . MM  ....  MM   .. :MMMMMM? ...   MMMMM=.  .
-- M+..8MM ..  M$ ..M....MM. MM, ...MMM  ..MMM. ....MMMM.. MM  .... .MM.. . MMM.  .MMM... . MM  MM...
-- M+..MM ..........M... MM ~MM.   ~MM.     ZMD   . MM=MM  MM        MM.  .~MN.     ZMZ.    MM.IMM
-- M+..MN ......... M..  MMMMN     OMN.     .MM   . MM~,MM MM        MM.   OM$       MM.    MMMMMM,
-- M+..MM. .... ....M... MM MM....  MM.     MMI   . MM~ .MMMM     .  MM.  . MM.     MM~.    MM.  MM
-- M+...MMM$~NMMM ..M....MM .MM  ....MMMI=MMM$... ..MM~  .MMM ... MMIMM.  . .MMM?+MMM7..  ..MMMMMMN
-- M+....:MMMM8  .. M..  MM  ~MM    . ~NMMM+      . MM~    MM     ~MMM .      ~NMMM~.       MMMM$
-- M+...............M........... ......................................................................
-- MMMMMMMMMMMMMMMMMM.~MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.
-- Cronjob
CREATE SEQUENCE SEQ_CRON_JOB_ID START WITH 1;
CREATE TABLE CRON_JOB(
    JOB_ID              number                               default SEQ_CRON_JOB_ID.nextval,
    JOB_GROUP           varchar2(20)                         not null,
    JOB_NAME            varchar2(80)                         not null,
    JOB_CLASS           varchar2(100)                        not null,
    CRON_EXP            varchar2(100)                        not null,

    STATUS	             varchar2(2)                          not null,
    MANDATORY           varchar2(1)                          not null,

    CREATEDATE          date default SYSDATE not null,
    CREATEBY            varchar2(10)                         not null,
    MODIFYDATE          date default SYSDATE not null,
    MODIFYBY            varchar2(10)                         not null,
    REMARKS             varchar2(250),
    CONSTRAINT PK_CRON_JOB PRIMARY KEY (JOB_ID)
);
CREATE UNIQUE INDEX IDX_CRON_JOB_1 ON CRON_JOB (JOB_GROUP, JOB_NAME);

CREATE OR REPLACE TRIGGER TRIGGER_CRON_JOB_1
    BEFORE INSERT ON CRON_JOB
    FOR EACH ROW
BEGIN
    :NEW.CREATEDATE := SYSDATE;
END;

CREATE OR REPLACE TRIGGER TRIGGER_CRON_JOB_2
    BEFORE UPDATE ON CRON_JOB
    FOR EACH ROW
BEGIN
    :NEW.CREATEDATE := :OLD.CREATEDATE;
    :NEW.CREATEBY := :OLD.CREATEBY;
    :NEW.MODIFYDATE := SYSDATE;
END;

CREATE TABLE CRON_JOB_LOG(
    SERVER_HOSTNAME     varchar2(50),

    JOB_GROUP           varchar2(20)                         not null,
    JOB_NAME            varchar2(80)                         not null,
    JOB_CLASS           varchar2(100)                        not null,

    ACTION              varchar2(10),
    SERVER_IP           varchar2(50),

    CREATEDATE          date default SYSDATE not null,
    CREATEBY            number                                 not null,
    MODIFYDATE          date default SYSDATE not null
);
CREATE INDEX IDX_CRON_JOB_LOG_1 ON CRON_JOB_LOG (SERVER_IP);
CREATE INDEX IDX_CRON_JOB_LOG_2 ON CRON_JOB_LOG (JOB_CLASS);
CREATE INDEX IDX_CRON_JOB_LOG_3 ON CRON_JOB_LOG (CREATEDATE);

CREATE OR REPLACE TRIGGER TRIGGER_CRON_JOB_LOG_1
    BEFORE INSERT ON CRON_JOB_LOG
    FOR EACH ROW
BEGIN
    :NEW.CREATEDATE := SYSDATE;
END;

CREATE OR REPLACE TRIGGER TRIGGER_CRON_JOB_LOG_2
    BEFORE UPDATE ON CRON_JOB_LOG
    FOR EACH ROW
BEGIN
    :NEW.CREATEDATE := :OLD.CREATEDATE;
    :NEW.CREATEBY := :OLD.CREATEBY;
    :NEW.MODIFYDATE := SYSDATE;
END;






-- MMMMMMMMMMMMMMMMMMMMMMMMMMMN                                  .                        .
-- .MMOOOOOOOOOOOOOOOOOOOOOOO8MN .    .        .
--  MM... .      .....       ~MN .    .   .    .               .                        .
--  MM. ........888. ........~MN.....888 .....888 ........88888$+. ...........888 ......888888888 .....
--  MM. .......MMMMM.........~MN.....MMM .....MMM.........MMMMMMMMMM .........MMM.......MMMMMMMMM......
--  MM.  .....:MMMMM  .......~MN.....MMM .....MMM.........MMMIIOMMMMMI........MMM.......OOOMMMOOO......
--  MM........MMM=MMM........~MN.....MMM .....MMM.........MMM.... .MMM: ......MMM..........MMM.........
--  MM ......NMMM.8MM=.......~MN.....MMM .....MMM.........MMM......ZMMM.......MMM..........MMM.........
--  MM ..... MMM...MMM ......~MN.....MMM .....MMM.........MMM.......MMM.......MMM..........MMM ........
--  MM  ....MMMD ..?MM8 .....~MN.....MMM .....MMM.........MMM...... MMM ......MMM..........MMM.........
--  MM ....,MMMMMMMMMMM+.....~MN.....MMM .....MMM ........MMM......MMMM.......MMM..........MMM.........
--  MM  ...MMMMMMMMMMMMM.....~MN.....DMM~.....MMM ........MMM.....DMMM .......MMM..........MMM.........
--  MM   .+MMN ......MMM?....~MN......MMMN~ ?MMMM.........MMMMMMMMMMM? .......MMM..........MMM.........
--  MM  ..MMM ....... MMM....~MN.......MMMMMMMM?..........MMMMMMMMM:..........MMM..........MMM.........
--  MM   ....................~MN......... .~   ........................................................
--  MM.. ....................~MN.......................................................................
--  MMIIIIIIIIIIIIIIIIIIIIIII$MN..NNNNNNNNNNNNNNNNNNNN8888888888888888888888888888888888888888888888? .
-- MMMMMMMMMMMMMMMMMMMMMMMMMMMN..MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMI..
-- Audit
CREATE SEQUENCE SEQ_AUDIT_TRAIL_ID START WITH 1;
CREATE TABLE AUDIT_TRAIL(
    AUDIT_ID        number                default SEQ_AUDIT_TRAIL_ID.nextval,
    USER_ID         varchar2(10)          not null,
    ACTION          varchar2(20)          not null,
    DETAIL          varchar2(200),
    CREATEDATE      date                  default SYSDATE not null,
    MODIFYDATE      date                  default SYSDATE not null,
    CONSTRAINT PK_AUDIT_TRAIL PRIMARY KEY (AUDIT_ID)
);
CREATE INDEX IDX_AUDIT_TRAIL_1 ON AUDIT_TRAIL (USER_ID);
CREATE INDEX IDX_AUDIT_TRAIL_2 ON AUDIT_TRAIL (ACTION);
CREATE INDEX IDX_AUDIT_TRAIL_3 ON AUDIT_TRAIL (CREATEDATE);

CREATE OR REPLACE TRIGGER TRIGGER_AUDIT_TRAIL_1
    BEFORE INSERT ON AUDIT_TRAIL
    FOR EACH ROW
BEGIN
    :NEW.CREATEDATE := SYSDATE;
END;

CREATE OR REPLACE TRIGGER TRIGGER_AUDIT_TRAIL_2
    BEFORE UPDATE ON AUDIT_TRAIL
    FOR EACH ROW
BEGIN
    :NEW.CREATEDATE := :OLD.CREATEDATE;
    :NEW.MODIFYDATE := SYSDATE;
END;





-- .~MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMO. .  .. .  . .  . . .      .         .         .     .
--   MM.  .....................    IMO.                          .         .   .     .      .
--  .MM............................IMO. ..... . .... ......   .  . ...  .. . . . .   .  .....  . .
-- ..MM... .  OMMM.     MMMM      .IMO.      ,MMMMMMN        ..  MMMMMMMMMMM.. . .  OMMMMMMMM:..   .
--  .MM...    OMMM      MMMM.   ...IMO. . ..7MMMMMMMMMO   .  .   MMMMMMMMMMM.       OMMMMMMMMMM,..
--  .MM .. .  OMMM      MMMM.   ...IMO. . ..MMMM  .NMM..  .      MMMMIIIIIII        OMMMIIOMMMMM
--  .MM... .  OMMM      MMMM.   ...IMO. .  .MMM8  ..  ..  .  ..  MMMM      .     .  OMMM  ..+MMM.
--  .MM... .. OMMM      MMMM.     .IMO.     OMMMD. .         ..  MMMM.       .   .  OMMM . .7MMM.. .
-- ..MM...    OMMM      MMMM.   ...IMO. . .. :MMMMM=  ..  .......MMMMMMMMMMM.... ...OMMMMMMMMMMM . ...
-- ..MM...    OMMM      MMMM.  ....IMO.........,MMMMMM ..........MMMMMMMMMMM........OMMMMMMMMM: .......
-- ..MM.....  OMMM      MMMM.  ....IMO...........~MMMMD..........MMMM ..............OMMMIMMM8 .. . ...
-- ..MM...    $MMM .    MMMM   ....IMO. . .. ....  DMMM= .. ...  MMMM   .. .......  OMMM 8MMMI.. . .  .
--  .MM... .. =MMM7.    MMMM     ..IMO. . ..$MM.  .~MMM=  .  ..  MMMM   .. . . . .  OMMM .NMMM$  . .
-- ..MM...     MMMM$,.,NMMM7   ....IMO......MMMM..,MMMM ...  ....MMMM7777777........OMMM . MMMM=....
-- ..MM... .    MMMMMMMMMMM.    ...IMO... ..ZMMMMMMMMMO.. .  ..  MMMMMMMMMMM..   .  OMMM . .MMMM . .
-- ..MM... .    .=NMMMMM$.      ...IMO. . .. :8MMMMM$...  .  ... MMMMMMMMMMM.. ...  OMMM ....MMMM  .
--  .MM...           .          ...IMO. . ..  .. . . ...  .  ..  . ...  .. . . . .  ..  ..... .. . .
--  .MM...      .               . .IMO. . ..   .  ..  ..  .  ..  . ...  .. . . . .  ..  ..... .... .
-- ..MM::::::::::::::::::::::::::::7MO. IDDDDDDDDDDDDDDDDDDDDZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ
-- ..MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMO. 7MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
-- User
CREATE TABLE USER_PROFILE (
  USER_ID               varchar2(10),
  NAME                  varchar2(100),
  STATUS                varchar2(2),

  MOBILE                varchar2(15),
  EMAIL                 varchar2(50)                    not null,

  LDAP_DOMAIN           varchar2(50),
  PASSWORD              varchar2(100),
  PASSWORD_MODIFYDATE   date,
  LOGIN_TRIED           number default 0,

  CREATEDATE            date     default SYSDATE        not null,
  CREATEBY              varchar2(10)                    not null,
  MODIFYDATE            date     default SYSDATE        not null,
  MODIFYBY              varchar2(10)                    not null,
  REMARKS               varchar2(250),
  CONSTRAINT PK_USER_PROFILE PRIMARY KEY (USER_ID)
);
CREATE UNIQUE INDEX IDX_USER_PROFILE_1 ON USER_PROFILE (EMAIL);
CREATE SEQUENCE SEQ_USER_PROFILE_USER_ID START WITH 1;

CREATE OR REPLACE TRIGGER TRIGGER_USER_PROFILE_1
    BEFORE INSERT ON USER_PROFILE
    FOR EACH ROW
BEGIN
    :NEW.CREATEDATE := SYSDATE;
END;

CREATE OR REPLACE TRIGGER TRIGGER_USER_PROFILE_2
    BEFORE UPDATE ON USER_PROFILE
    FOR EACH ROW
BEGIN
    :NEW.CREATEDATE := :OLD.CREATEDATE;
    :NEW.CREATEBY := :OLD.CREATEBY;
    :NEW.MODIFYDATE := SYSDATE;
END;


CREATE TABLE USER_USER_ROLE(
    USER_ID               varchar2(10)                not null,
    ROLE_ID               varchar2(20)                not null
);

CREATE INDEX IDX_USER_USER_ROLE_1 ON USER_USER_ROLE (USER_ID);
CREATE INDEX IDX_USER_USER_ROLE_2 ON USER_USER_ROLE (ROLE_ID);
CREATE UNIQUE INDEX IDX_USER_USER_ROLE_3 ON USER_USER_ROLE (USER_ID,ROLE_ID);

CREATE TABLE USER_PWD_HIST(
  USER_ID               varchar2(10)                not null,
  PASSWORD              varchar2(100)               not null,
  CREATEDATE            date    default SYSDATE     not null
);

-- otp
CREATE TABLE OTP(
    USER_ID             varchar2(10)                not null,
    ACTION              varchar2(20)                not null,
    OTP                 varchar2(40)                not null,
    EXPIRYDATE          date                        not null,

    CREATEDATE          date    default SYSDATE     not null,
    CREATEBY            varchar2(10)                not null,
    MODIFYDATE          date    default SYSDATE     not null
);
CREATE INDEX IDX_OTP_1 ON OTP (USER_ID);
CREATE INDEX IDX_OTP_2 ON OTP (ACTION);

CREATE OR REPLACE TRIGGER TRIGGER_OTP_1
    BEFORE INSERT ON OTP
    FOR EACH ROW
BEGIN
    :NEW.CREATEDATE := SYSDATE;
END;

CREATE OR REPLACE TRIGGER TRIGGER_OTP_2
    BEFORE UPDATE ON OTP
    FOR EACH ROW
BEGIN
    :NEW.CREATEDATE := :OLD.CREATEDATE;
    :NEW.CREATEBY := :OLD.CREATEBY;
    :NEW.MODIFYDATE := SYSDATE;
END;


-- User Role
CREATE TABLE USER_ROLE(
  ROLE_ID               varchar2(20),
  ROLE_DESC             varchar2(50)                    not null,
  PARENT_ROLE_ID        varchar2(20),
  STATUS                varchar2(2),

  CREATEDATE            date     default SYSDATE        not null,
  CREATEBY              varchar2(10)                    not null,
  MODIFYDATE            date     default SYSDATE        not null,
  MODIFYBY              varchar2(10)                    not null,
  REMARKS               varchar2(250),

  CONSTRAINT PK_USER_ROLE PRIMARY KEY (ROLE_ID)
);

CREATE OR REPLACE TRIGGER TRIGGER_USER_ROLE_1
    BEFORE INSERT ON USER_ROLE
    FOR EACH ROW
BEGIN
    :NEW.CREATEDATE := SYSDATE;
END;

CREATE OR REPLACE TRIGGER TRIGGER_USER_ROLE_2
    BEFORE UPDATE ON USER_ROLE
    FOR EACH ROW
BEGIN
    :NEW.CREATEDATE := :OLD.CREATEDATE;
    :NEW.CREATEBY := :OLD.CREATEBY;
    :NEW.MODIFYDATE := SYSDATE;
END;




CREATE TABLE USER_ROLE_PATH_CTRL(
    ROLE_ID         varchar2(20)                            not null,
    PATH_CTRL_ID    number                                  not null,
    CREATEDATE      date           default SYSDATE          not null,
    CREATEBY        varchar2(10)                            not null,
    REMARKS         varchar2(250)
);
CREATE UNIQUE INDEX IDX_USER_ROLE_PATH_CTRL_1 ON USER_ROLE_PATH_CTRL (ROLE_ID, PATH_CTRL_ID);
CREATE INDEX IDX_USER_ROLE_PATH_CTRL_2 ON USER_ROLE_PATH_CTRL (ROLE_ID);
CREATE INDEX IDX_USER_ROLE_PATH_CTRL_3 ON USER_ROLE_PATH_CTRL (PATH_CTRL_ID);



-- url path control
CREATE SEQUENCE SEQ_PATH_CTRL_ID START WITH 1;
CREATE TABLE PATH_CTRL(
                          PATH_CTRL_ID    number        default SEQ_PATH_CTRL_ID.nextval  not null,
                          PATH            varchar2(200)                                   not null,
                          STATUS          varchar2(2)                                     null,
                          DESCRIPTION     varchar2(50)                                    not null,
                          CREATEDATE      date          default SYSDATE                   not null,
                          CREATEBY        varchar2(10)                                    not null,
                          MODIFYDATE      date          default SYSDATE                   not null,
                          MODIFYBY        varchar2(10)                                    not null,
                          REMARKS         varchar2(250),
                          CONSTRAINT PK_PATH_CTRL PRIMARY KEY (PATH_CTRL_ID)
);
CREATE OR REPLACE TRIGGER TRIGGER_PATH_CTRL_1
    BEFORE INSERT ON PATH_CTRL
    FOR EACH ROW
BEGIN
    :NEW.CREATEDATE := SYSDATE;
END;
CREATE OR REPLACE TRIGGER TRIGGER_PATH_CTRL_2
    BEFORE UPDATE ON PATH_CTRL
    FOR EACH ROW
BEGIN
    :NEW.CREATEDATE := :OLD.CREATEDATE;
    :NEW.CREATEBY := :OLD.CREATEBY;
    :NEW.MODIFYDATE := SYSDATE;
END;


-- SQL report
CREATE SEQUENCE  SEQ_SQL_REPORT_REPORT_ID START WITH 1;
CREATE TABLE SQL_REPORT
(
    REPORT_ID   NUMBER  default  SEQ_SQL_REPORT_REPORT_ID.nextval  not null,
    REPORT_NAME VARCHAR2(20)         not null,
    CRON_EXP    VARCHAR2(20)         not null,
    STATUS      VARCHAR2(2)          not null,
    SQL         VARCHAR2(100)        not null,
    EXPORT_TO   VARCHAR2(50),
    EMAIL_TO    VARCHAR2(20),
    CREATEDATE  DATE default SYSDATE not null,
    CREATEBY    VARCHAR2(10)         not null,
    MODIFYDATE  DATE default SYSDATE not null,
    MODIFYBY    VARCHAR2(10)         not null,
    REMARKS     VARCHAR2(250),
    constraint SQL_REPORT_PK primary key (REPORT_ID)
);
CREATE INDEX IDX_SQL_REPORT ON SQL_REPORT (REPORT_ID);