CREATE TABLE CONFIG_PARAM_HIST(
  CONFIG_GROUP      VARCHAR2(40),
  CONFIG_KEY        VARCHAR2(40),
  CONFIG_VALUE      VARCHAR2(400),
  CONFIG_VALUE_TYPE VARCHAR2(20),
  ENCRYPT           VARCHAR2(1),
  ACTION            VARCHAR2(1),
  CREATEDATE        DATE                    default SYSDATE not null
);
CREATE INDEX IDX_CONFIG_PARAM_HIST_1 ON CONFIG_PARAM_HIST (CONFIG_GROUP, CONFIG_KEY);
CREATE INDEX IDX_CONFIG_PARAM_HIST_2 ON CONFIG_PARAM_HIST (CREATEDATE);


CREATE OR REPLACE TRIGGER TRIGGER_CONFIG_PARAM_HIST
    FOR UPDATE OR DELETE
    ON CONFIG_PARAM COMPOUND TRIGGER
    --
    -- an array structure to buffer all the row changes
    --
    type t_row_list is
        table of CONFIG_PARAM_HIST%rowtype index by pls_integer;

    l_audit_rows      t_row_list;

    l_operation varchar2(1) :=
        case when updating then 'U'
             when deleting then 'D'
            end;

    procedure insert_logged_so_far is
    begin
        forall i in 1 .. l_audit_rows.count
            insert into CONFIG_PARAM_HIST
            values l_audit_rows(i);
        l_audit_rows.delete;
    end;

before statement is
begin
    --
    -- initialize the array
    --
    l_audit_rows.delete;
end before statement;

    after each row is
    begin
        --
        -- at row level, capture all the changes into the array
        --
        l_audit_rows(l_audit_rows.count+1).createdate      := sysdate;
        l_audit_rows(l_audit_rows.count).action            := l_operation;

        if updating or deleting then
            l_audit_rows(l_audit_rows.count).config_group         := :old.config_group;
            l_audit_rows(l_audit_rows.count).config_key           := :old.config_key;
            l_audit_rows(l_audit_rows.count).config_value         := :old.config_value;
            l_audit_rows(l_audit_rows.count).config_value_type    := :old.config_value_type;
            l_audit_rows(l_audit_rows.count).encrypt              := :old.encrypt;
        end if;

        --
        -- bulk bind every 1000 rows to keep the memory down
        --
        if l_audit_rows.count > 1000 then
            insert_logged_so_far;
        end if;
    end after each row;

    after statement is
    begin
        --
        -- then at completion, pick up the remaining rows
        --
        if l_audit_rows.count > 0 then
            insert_logged_so_far;
        end if;
    end after statement;
    END;

-------------------------------------------------------------------------------------------------
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
CREATE INDEX IDX_CRON_JOB_HIST_1 ON CRON_JOB_HIST (JOB_ID);
CREATE INDEX IDX_CRON_JOB_HIST_2 ON CRON_JOB_HIST (CREATEDATE);

CREATE OR REPLACE TRIGGER TRIGGER_CRON_JOB_HIST
    FOR UPDATE OR DELETE
    ON CRON_JOB COMPOUND TRIGGER

    type t_row_list is
        table of CRON_JOB_HIST%rowtype index by pls_integer;

    l_audit_rows      t_row_list;

    l_operation varchar2(1) :=
        case when updating then 'U'
             when deleting then 'D'
            end;

    procedure insert_logged_so_far is
    begin
        forall i in 1 .. l_audit_rows.count
            insert into CRON_JOB_HIST
            values l_audit_rows(i);
        l_audit_rows.delete;
    end;

before statement is
begin

    l_audit_rows.delete;
end before statement;

    after each row is
    begin
        l_audit_rows(l_audit_rows.count+1).createdate      := sysdate;
        l_audit_rows(l_audit_rows.count).action            := l_operation;

        if updating or deleting then
            l_audit_rows(l_audit_rows.count).job_id        := :old.job_id;
            l_audit_rows(l_audit_rows.count).job_group     := :old.job_group;
            l_audit_rows(l_audit_rows.count).job_name      := :old.job_name;
            l_audit_rows(l_audit_rows.count).job_class     := :old.job_class;
            l_audit_rows(l_audit_rows.count).cron_exp      := :old.cron_exp;
            l_audit_rows(l_audit_rows.count).status        := :old.status;
            l_audit_rows(l_audit_rows.count).mandatory     := :old.mandatory;
        end if;

        if l_audit_rows.count > 1000 then
            insert_logged_so_far;
        end if;
    end after each row;

    after statement is
    begin
        if l_audit_rows.count > 0 then
            insert_logged_so_far;
        end if;
    end after statement;
    END;

-------------------------------------------------------------------------------------------------
CREATE TABLE PATH_CTRL_HIST(
    PATH_CTRL_ID    number                  not null,
    PATH            varchar2(200)           not null,
    STATUS          varchar2(2)             null,
    DESCRIPTION     varchar2(50)            not null,
    ACTION          VARCHAR2(1)             not null,
    CREATEDATE      DATE                    default SYSDATE not null
);
CREATE INDEX IDX_PATH_CTRL_HIST_1 ON PATH_CTRL_HIST (PATH_CTRL_ID);
CREATE INDEX IDX_PATH_CTRL_HIST_2 ON PATH_CTRL_HIST (CREATEDATE);

CREATE OR REPLACE TRIGGER TRIGGER_PATH_CTRL_HIST
    FOR UPDATE OR DELETE
    ON PATH_CTRL COMPOUND TRIGGER

    type t_row_list is
        table of PATH_CTRL_HIST%rowtype index by pls_integer;

    l_audit_rows      t_row_list;

    l_operation varchar2(1) :=
        case when updating then 'U'
             when deleting then 'D'
            end;

    procedure insert_logged_so_far is
    begin
        forall i in 1 .. l_audit_rows.count
            insert into PATH_CTRL_HIST
            values l_audit_rows(i);
        l_audit_rows.delete;
    end;

before statement is
begin
    l_audit_rows.delete;
end before statement;

    after each row is
    begin
        l_audit_rows(l_audit_rows.count+1).createdate      := sysdate;
        l_audit_rows(l_audit_rows.count).action            := l_operation;

        if updating or deleting then
            l_audit_rows(l_audit_rows.count).path_ctrl_id     := :old.path_ctrl_id;
            l_audit_rows(l_audit_rows.count).path             := :old.path;
            l_audit_rows(l_audit_rows.count).status           := :old.status;
            l_audit_rows(l_audit_rows.count).description      := :old.description;
        end if;

        if l_audit_rows.count > 1000 then
            insert_logged_so_far;
        end if;
    end after each row;

    after statement is
    begin
        if l_audit_rows.count > 0 then
            insert_logged_so_far;
        end if;
    end after statement;
    END;

-------------------------------------------------------------------------------------------------
CREATE TABLE USER_ROLE_HIST(
  ROLE_ID               varchar2(20),
  ROLE_DESC             varchar2(50)                    not null,
  PARENT_ROLE_ID        varchar2(20),
  STATUS                varchar2(2),
  ABSTRACT              varchar2(2),
  ACTION                VARCHAR2(1)                     not null,
  CREATEDATE            DATE                            default SYSDATE not null
);
CREATE INDEX IDX_USER_ROLE_HIST_1 ON USER_ROLE_HIST (ROLE_ID);
CREATE INDEX IDX_USER_ROLE_HIST_2 ON USER_ROLE_HIST (CREATEDATE);

CREATE OR REPLACE TRIGGER TRIGGER_USER_ROLE_HIST
    FOR UPDATE OR DELETE
    ON USER_ROLE COMPOUND TRIGGER

    type t_row_list is
        table of USER_ROLE_HIST%rowtype index by pls_integer;

    l_audit_rows      t_row_list;

    l_operation varchar2(1) :=
        case when updating then 'U'
             when deleting then 'D'
            end;

    procedure insert_logged_so_far is
    begin
        forall i in 1 .. l_audit_rows.count
            insert into USER_ROLE_HIST
            values l_audit_rows(i);
        l_audit_rows.delete;
    end;

before statement is
begin
    l_audit_rows.delete;
end before statement;

    after each row is
    begin
        l_audit_rows(l_audit_rows.count+1).createdate      := sysdate;
        l_audit_rows(l_audit_rows.count).action            := l_operation;

        if updating or deleting then
            l_audit_rows(l_audit_rows.count).role_id          := :old.role_id;
            l_audit_rows(l_audit_rows.count).role_desc        := :old.role_desc;
            l_audit_rows(l_audit_rows.count).parent_role_id   := :old.parent_role_id;
            l_audit_rows(l_audit_rows.count).status           := :old.status;
            l_audit_rows(l_audit_rows.count).ABSTRACT         := :old.ABSTRACT;
        end if;

        if l_audit_rows.count > 1000 then
            insert_logged_so_far;
        end if;
    end after each row;

    after statement is
    begin
        if l_audit_rows.count > 0 then
            insert_logged_so_far;
        end if;
    end after statement;
    END;

-------------------------------------------------------------------------------------------------
CREATE TABLE USER_ROLE_PATH_CTRL_HIST(
    ROLE_ID         varchar2(20)                            not null,
    PATH_CTRL_ID    number                                  not null,
    ACTION          VARCHAR2(1)                             not null,
    CREATEDATE      DATE                                    default SYSDATE not null
);
CREATE INDEX IDX_USER_ROLE_PATH_CTRL_HIST_1 ON USER_ROLE_PATH_CTRL_HIST (ROLE_ID, PATH_CTRL_ID);
CREATE INDEX IDX_USER_ROLE_PATH_CTRL_HIST_2 ON USER_ROLE_PATH_CTRL_HIST (CREATEDATE);

CREATE OR REPLACE TRIGGER TRIGGER_USER_R_PATH_C_HIST
    FOR UPDATE OR DELETE
    ON USER_ROLE_PATH_CTRL COMPOUND TRIGGER

    type t_row_list is
        table of USER_ROLE_PATH_CTRL_HIST%rowtype index by pls_integer;

    l_audit_rows      t_row_list;

    l_operation varchar2(1) :=
        case when updating then 'U'
             when deleting then 'D'
            end;

    procedure insert_logged_so_far is
    begin
        forall i in 1 .. l_audit_rows.count
            insert into USER_ROLE_PATH_CTRL_HIST
            values l_audit_rows(i);
        l_audit_rows.delete;
    end;

before statement is
begin
    l_audit_rows.delete;
end before statement;

    after each row is
    begin
        l_audit_rows(l_audit_rows.count+1).createdate      := sysdate;
        l_audit_rows(l_audit_rows.count).action            := l_operation;

        if updating or deleting then
            l_audit_rows(l_audit_rows.count).role_id          := :old.role_id;
            l_audit_rows(l_audit_rows.count).path_ctrl_id     := :old.path_ctrl_id;
        end if;

        if l_audit_rows.count > 1000 then
            insert_logged_so_far;
        end if;
    end after each row;

    after statement is
    begin
        if l_audit_rows.count > 0 then
            insert_logged_so_far;
        end if;
    end after statement;
    END;
-------------------------------------------------------------------------------------------------
CREATE TABLE USER_USER_ROLE_HIST(
    USER_ID               varchar2(10)                not null,
    ROLE_ID               varchar2(20)                not null,
    ACTION                VARCHAR2(1)                 not null,
    CREATEDATE            DATE                        default SYSDATE not null
);
CREATE INDEX IDX_USER_USER_ROLE_HIST_1 ON USER_USER_ROLE_HIST (USER_ID, ROLE_ID);
CREATE INDEX IDX_USER_USER_ROLE_HIST_2 ON USER_USER_ROLE_HIST (CREATEDATE);

CREATE OR REPLACE TRIGGER TRIGGER_USER_USER_ROLE_HIST
    FOR UPDATE OR DELETE
    ON USER_USER_ROLE COMPOUND TRIGGER

    type t_row_list is
        table of USER_USER_ROLE_HIST%rowtype index by pls_integer;

    l_audit_rows      t_row_list;

    l_operation varchar2(1) :=
        case when updating then 'U'
             when deleting then 'D'
            end;

    procedure insert_logged_so_far is
    begin
        forall i in 1 .. l_audit_rows.count
            insert into USER_USER_ROLE_HIST
            values l_audit_rows(i);
        l_audit_rows.delete;
    end;

before statement is
begin
    l_audit_rows.delete;
end before statement;

    after each row is
    begin
        l_audit_rows(l_audit_rows.count+1).createdate      := sysdate;
        l_audit_rows(l_audit_rows.count).action            := l_operation;

        if updating or deleting then
            l_audit_rows(l_audit_rows.count).user_id        := :old.user_id;
            l_audit_rows(l_audit_rows.count).role_id        := :old.role_id;
        end if;

        if l_audit_rows.count > 1000 then
            insert_logged_so_far;
        end if;
    end after each row;

    after statement is
    begin
        if l_audit_rows.count > 0 then
            insert_logged_so_far;
        end if;
    end after statement;
    END;

-------------------------------------------------------------------------------------------------















