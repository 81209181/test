-- User Role
insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('TEAM_A', 'Team A', 'CS_AGENT', 'A', '81149189', '81149189', 'Team A');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('TEAM_B', 'Team B', 'CS_AGENT', 'A', '81149189', '81149189', 'Team B');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('TH__TEAM_A', 'Team A Head', 'TEAM_HEAD', 'A', '81149189', '81149189', 'Team A Head');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('TH__TEAM_B', 'Team B Head ', 'TEAM_HEAD', 'A', '81149189', '81149189', 'Team B Head');
