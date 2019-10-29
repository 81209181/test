-- User Role
insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('O_TEAM_A', 'Team A', 'OPT', 'A', '81149189', '81149189', 'Team A');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('O_TEAM_B', 'Team B', 'OPT', 'A', '81149189', '81149189', 'Team B');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('TH__O_TEAM_A', 'Team A Head', 'TEAM_HEAD', 'A', '81149189', '81149189', 'Team A Head');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('TH__O_TEAM_B', 'Team B Head ', 'TEAM_HEAD', 'A', '81149189', '81149189', 'Team B Head');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('E_TEAM_A', 'Engineer', 'ENG', 'A', '81149189', '81149189', 'Engineer');

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('TH__E_TEAM_A', 'Team A Engineer Head', 'TEAM_HEAD', 'A', '81149189', '81149189', 'Team A Engineer Head');
