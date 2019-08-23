-- User Role
insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('BASIC', 'BASIC', NULL, 'A', '81149189', '81149189', 'BASIC ROLE');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('SYS_ADMIN', 'System Admin', NULL, 'A', '81149189', '81149189', 'System Admin');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('CS_AGENT', 'Agent', 'BASIC', 'A', '81149189', '81149189', 'AGENT');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('TEAM_A', 'Team A', 'CS_AGENT', 'A', '81149189', '81149189', 'Team A');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('TEAM_B', 'Team B', 'CS_AGENT', 'A', '81149189', '81149189', 'Team B');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('TEAM_HEAD', 'Team Head', 'BASIC', 'A', '81149189', '81149189', 'Team Head');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('TH__TEAM_A', 'Team A Head', 'TEAM_HEAD', 'A', '81149189', '81149189', 'Team A Head');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('TH__TEAM_B', 'Team B Head ', 'TEAM_HEAD', 'A', '81149189', '81149189', 'Team B Head');
