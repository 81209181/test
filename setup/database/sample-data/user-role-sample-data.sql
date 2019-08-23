-- User Role
insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('1', 'BASIC', NULL, 'A', '81149189', '81149189','BASIC ROLE');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('2', 'SYS_ADMIN', NULL, 'A', '81149189', '81149189', 'System Admin');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('3', 'CS_AGENT', '1', 'A', '81149189', '81149189','AGENT');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('4', 'TEAM_A', '3', 'A', '81149189', '81149189', 'Team A');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('5', 'TEAM_B', '3', 'A', '81149189', '81149189', 'Team B');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('6', 'TEAM_HEAD', '1', 'A', '81149189', '81149189', 'Team Head');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('7', 'TH_TEAM_A', '6', 'A', '81149189', '81149189', 'Team A Head');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('8', 'TH_TEAM_B', '6', 'A', '81149189', '81149189', 'Team B Head');
