CREATE SEQUENCE SEQ_TICKET_MAS_ID START WITH 1;
CREATE TABLE TICKET_MAS
(
    TICKET_MAS_ID      number                  default SEQ_TICKET_MAS_ID.nextval,
    CUST_CODE          varchar2(40),
    TICKET_TYPE        varchar2(5), -- {'QUERY', 'FAULT'}
    STATUS             varchar2(2), -- {OPEN: 'O', CANCEL: 'CX', COMPLETE: 'CP'}

    CREATEDATE         date                    default SYSDATE not null,
    CREATEBY           varchar2(10)            not null,
    MODIFYDATE         date                    default SYSDATE not null,
    MODIFYBY           varchar2(10)            not null,
    CONSTRAINT PK_TICKET_MAS PRIMARY KEY (TICKET_MAS_ID)
);
CREATE INDEX IDX_TICKET_MAS_1 ON TICKET_MAS (CUST_CODE);
CREATE INDEX IDX_TICKET_MAS_2 ON TICKET_MAS (CREATEDATE);

--------------------------------------------------------------------------------------------------
CREATE SEQUENCE SEQ_TICKET_DET_ID START WITH 1;
CREATE TABLE TICKET_DET
(
    TICKET_DET_ID      number                  default SEQ_TICKET_DET_ID.nextval,
    TICKET_MAS_ID      number,

    SERVICE_TYPE_CODE  varchar2(20)             not null,
    SERVICE_ID         varchar2(30)             not null,

    CREATEDATE         date                     default SYSDATE not null,
    CREATEBY           varchar2(10)             not null,
    MODIFYDATE         date                     default SYSDATE not null,
    MODIFYBY           varchar2(10)             not null,
    CONSTRAINT PK_TICKET_DET PRIMARY KEY (TICKET_DET_ID)
);
CREATE INDEX IDX_TICKET_DET_1 ON TICKET_DET (TICKET_MAS_ID);
CREATE INDEX IDX_TICKET_DET_2 ON TICKET_DET (CREATEDATE);
CREATE INDEX IDX_TICKET_DET_3 ON TICKET_DET (SERVICE_ID);

--------------------------------------------------------------------------------------------------
CREATE SEQUENCE SEQ_TICKET_CONTACT_ID START WITH 1;
CREATE TABLE TICKET_CONTACT
(
    TICKET_CONTACT_ID       number                  default SEQ_TICKET_CONTACT_ID.nextval,
    TICKET_MAS_ID           number,

    CONTACT_TYPE            varchar2(5), -- {Customer: 'CUST', Office Admin: 'ADMIN', On-site Contact: 'SITE'}
    CONTACT_NAME            varchar2(100),
    CONTACT_NUMBER          varchar2(50),
    CONTACT_MOBILE          varchar2(20),
    CONTACT_EMAIL           varchar2(50),

    CREATEDATE         date                    default SYSDATE not null,
    CREATEBY           varchar2(10)            not null,
    MODIFYDATE         date                    default SYSDATE not null,
    MODIFYBY           varchar2(10)            not null,
    CONSTRAINT PK_TICKET_CONTACT PRIMARY KEY (TICKET_CONTACT_ID)
);
CREATE INDEX IDX_TICKET_CONTACT_1 ON TICKET_MAS (TICKET_MAS_ID);
CREATE INDEX IDX_TICKET_CONTACT_2 ON TICKET_MAS (CREATEDATE);

--------------------------------------------------------------------------------------------------
CREATE SEQUENCE SEQ_TICKET_REMARKS_ID START WITH 1;
CREATE TABLE TICKET_REMARKS
(
    TICKET_REMARKS_ID       number          default SEQ_TICKET_DET_ID.nextval,
    TICKET_MAS_ID           number,

    REMARKS_TYPE            varchar2(5), -- {Customer: 'CUST', Field: 'FIELD', System: 'SYS'}
    REMARKS                 varchar2(500),

    CREATEDATE              date            default SYSDATE not null,
    CREATEBY                varchar2(10)    not null,
    MODIFYDATE              date            default SYSDATE not null,
    MODIFYBY                varchar2(10)    not null,
    CONSTRAINT PK_TICKET_REMARKS PRIMARY KEY (TICKET_REMARKS_ID)
);
CREATE INDEX IDX_TICKET_REMARKS_1 ON TICKET_REMARKS (TICKET_MAS_ID);
CREATE INDEX IDX_TICKET_REMARKS_2 ON TICKET_REMARKS (CREATEDATE);






--------------------------------------------------------------------------------------------------
CREATE TABLE SERVICE_TYPE
(
    SERVICE_TYPE_CODE   varchar2(20),
    DESCRIPTION         varchar2(200),

    CREATEDATE          date                     default SYSDATE not null,
    CREATEBY            varchar2(10)             not null,
    MODIFYDATE          date                     default SYSDATE not null,
    MODIFYBY            varchar2(10)             not null,
    CONSTRAINT PK_TICKET_MAS PRIMARY KEY (SERVICE_TYPE_CODE)
);

--------------------------------------------------------------------------------------------------
-- CREATE TABLE FAULT_TYPE
-- (
--     FAULT_TYPE_CODE     varchar2(20),
--
--     CREATEDATE          date                     default SYSDATE not null,
--     CREATEBY            varchar2(10)             not null,
--     MODIFYDATE          date                     default SYSDATE not null,
--     MODIFYBY            varchar2(10)             not null,
--     CONSTRAINT PK_TICKET_MAS PRIMARY KEY (FAULT_TYPE_CODE)
-- );