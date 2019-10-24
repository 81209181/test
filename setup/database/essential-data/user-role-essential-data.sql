-- USER ROLE
insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('BASIC', 'Basic', NULL, 'A', '81149189', '81149189','BASIC ROLE');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('SYS_ADMIN', 'System Admin', 'BASIC', 'A', '81149189', '81149189', 'System Admin');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('TEAM_HEAD', 'Team Head', 'BASIC', 'A', '81149189', '81149189', 'Team Head');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('OPT', 'Operator', 'BASIC', 'A', '81149189', '81149189', 'Operator');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, ABSTRACT, CREATEBY, MODIFYBY, REMARKS)
values ('API', 'System API', 'BASIC', 'A', 'Y', '81149189', '81149189', null);
insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, ABSTRACT, CREATEBY, MODIFYBY, REMARKS)
values ('API_WFM', 'WFM Server', 'API', 'A', 'Y', '81149189', '81149189', null);