CREATE SEQUENCE SEQ_TICKET_MAS_ID START WITH 1;
CREATE TABLE TICKET_MAS
(
    TICKET_MAS_ID      number                  default SEQ_TICKET_MAS_ID.nextval,
    CUST_CODE          varchar2(40),
    TICKET_TYPE        varchar2(5), -- {'QUERY', 'FAULT'}
    STATUS             varchar2(2), -- {OPEN: 'O', CANCEL: 'CX', COMPLETE: 'CP'}
    COMPLETE_DATE      date,
    CALL_IN_COUNT      number                  default 0,
    SEARCH_KEY         varchar2(20),
    SEARCH_VALUE       varchar2(30),
    OWNING_ROLE        varchar2(20),
    CUST_NAME          varchar2(100),

    CREATEDATE         date                    default SYSDATE not null,
    CREATEBY           varchar2(12)            not null,
    MODIFYDATE         date                    default SYSDATE not null,
    MODIFYBY           varchar2(12)            not null,
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
    SYMPTOM_CODE       varchar2(10),
    SUBS_ID             varchar2(20),
    JOB_ID              varchar2(20),

    CREATEDATE         date                     default SYSDATE not null,
    CREATEBY           varchar2(12)             not null,
    MODIFYDATE         date                     default SYSDATE not null,
    MODIFYBY           varchar2(12)             not null,
    CONSTRAINT PK_TICKET_DET PRIMARY KEY (TICKET_DET_ID)
);
CREATE INDEX IDX_TICKET_DET_1 ON TICKET_DET (TICKET_MAS_ID);
CREATE INDEX IDX_TICKET_DET_2 ON TICKET_DET (CREATEDATE);
CREATE INDEX IDX_TICKET_DET_3 ON TICKET_DET (SERVICE_ID);
CREATE INDEX IDX_TICKET_DET_4 ON TICKET_DET (SUBS_ID);
CREATE INDEX IDX_TICKET_DET_5 ON TICKET_DET (JOB_ID);

--------------------------------------------------------------------------------------------------
CREATE SEQUENCE SEQ_TICKET_CONTACT_ID START WITH 1;
CREATE TABLE TICKET_CONTACT
(
    TICKET_CONTACT_ID       number                  default SEQ_TICKET_CONTACT_ID.nextval,
    TICKET_MAS_ID           number,

    CONTACT_TYPE            varchar2(5), -- {Customer: 'CUST', Office Admin: 'ADMIN', On-site Contact: 'SITE', Caller: 'CALL'}
    CONTACT_NAME            varchar2(100),
    CONTACT_NUMBER          BLOB,
    CONTACT_MOBILE          BLOB,
    CONTACT_EMAIL           BLOB,

    CREATEDATE         date                    default SYSDATE not null,
    CREATEBY           varchar2(12)            not null,
    MODIFYDATE         date                    default SYSDATE not null,
    MODIFYBY           varchar2(12)            not null,
    CONSTRAINT PK_TICKET_CONTACT PRIMARY KEY (TICKET_CONTACT_ID)
);
CREATE INDEX IDX_TICKET_CONTACT_1 ON TICKET_CONTACT (TICKET_MAS_ID);
CREATE INDEX IDX_TICKET_CONTACT_2 ON TICKET_CONTACT (CREATEDATE);

--------------------------------------------------------------------------------------------------
CREATE SEQUENCE SEQ_TICKET_REMARKS_ID START WITH 1;
CREATE TABLE TICKET_REMARKS
(
    TICKET_REMARKS_ID       number          default SEQ_TICKET_REMARKS_ID.nextval,
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
	SERVICE_TYPE_CODE VARCHAR2(10 BYTE) NOT NULL ENABLE, 
	SERVICE_TYPE_NAME VARCHAR2(30 BYTE) NOT NULL ENABLE, 
	CREATEDATE DATE DEFAULT SYSDATE NOT NULL ENABLE, 
	CREATEBY VARCHAR2(12 BYTE) NOT NULL ENABLE, 
	MODIFYDATE DATE DEFAULT SYSDATE NOT NULL ENABLE, 
	MODIFYBY VARCHAR2(12 BYTE) NOT NULL ENABLE, 
	CONSTRAINT PK_SERVICE_TYPE PRIMARY KEY (SERVICE_TYPE_CODE)	
);

--------------------------------------------------------------------------------------------------
CREATE TABLE SERVICE_TYPE_OFFER_MAPPING
(
    SERVICE_TYPE_CODE             varchar2(10)             not null,
    OFFER_NAME                    varchar2(100)             not null,
    CREATEDATE                    date                     default SYSDATE not null,
    CREATEBY                      varchar2(10)             not null,
    MODIFYDATE                    date                     default SYSDATE not null,
    MODIFYBY                      varchar2(10)             not null
);
CREATE UNIQUE INDEX IDX_SERVICE_TYPE_OFFER_MAPPING_1 ON SERVICE_TYPE_OFFER_MAPPING (SERVICE_TYPE_CODE, OFFER_NAME);
--------------------------------------------------------------------------------------------------
CREATE TABLE SYMPTOM_GROUP
(
    SYMPTOM_GROUP_CODE            varchar2(10)             not null,
    SYMPTOM_GROUP_NAME            varchar2(30)             not null,

    CREATEDATE                    date                     default SYSDATE not null,
    CREATEBY                      varchar2(10)             not null,
    MODIFYDATE                    date                     default SYSDATE not null,
    MODIFYBY                      varchar2(10)             not null,
    CONSTRAINT PK_SYMPTOM_GROUP PRIMARY KEY (SYMPTOM_GROUP_CODE)
);
--------------------------------------------------------------------------------------------------
CREATE TABLE SYMPTOM
(
    SYMPTOM_CODE                  varchar2(10)             not null,
    SYMPTOM_DESCRIPTION           varchar2(120)             not null,
    SYMPTOM_GROUP_CODE            varchar2(10)             not null,

    CREATEDATE                    date                     default SYSDATE not null,
    CREATEBY                      varchar2(10)             not null,
    MODIFYDATE                    date                     default SYSDATE not null,
    MODIFYBY                      varchar2(10)             not null,
    CONSTRAINT PK_SYMPTOM PRIMARY KEY (SYMPTOM_CODE)
);
CREATE INDEX IDX_SYMPTOM_1 ON SYMPTOM (SYMPTOM_GROUP_CODE);
--------------------------------------------------------------------------------------------------
CREATE TABLE SYMPTOM_MAPPING
(
    SERVICE_TYPE_CODE             varchar2(10)             not null,
    SYMPTOM_CODE                  varchar2(10)             not null,

    CREATEDATE                    date                     default SYSDATE not null,
    CREATEBY                      varchar2(10)             not null
);
CREATE INDEX IDX_SYMPTOM_MAPPING_1 ON SYMPTOM_MAPPING (SERVICE_TYPE_CODE);
CREATE INDEX IDX_SYMPTOM_MAPPING_2 ON SYMPTOM_MAPPING (SYMPTOM_CODE);
CREATE UNIQUE INDEX IDX_SYMPTOM_MAPPING_3 ON SYMPTOM_MAPPING (SERVICE_TYPE_CODE, SYMPTOM_CODE);

