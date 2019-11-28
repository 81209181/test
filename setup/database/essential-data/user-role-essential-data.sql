-- USER ROLE
-- level 0
insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('BASIC', 'Basic', NULL, 'A', '81149189', '81149189','BASIC ROLE');

-- level 1
insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('SYS_ADMIN', 'System Admin', 'BASIC', 'A', '81149189', '81149189', 'System Admin');
insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('TEAM_HEAD', 'Team Head', 'BASIC', 'A', '81149189', '81149189', 'Team Head');
insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('OPT', 'Operator', 'BASIC', 'A', '81149189', '81149189', 'Operator');
insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('ENG', 'Engineer', 'BASIC', 'A', '81149189', '81149189', 'Engineer');
insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, ABSTRACT, CREATEBY, MODIFYBY, REMARKS)
values ('API', 'System API', 'BASIC', 'A', 'Y', '81149189', '81149189', null);

-- level 2
insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, ABSTRACT, CREATEBY, MODIFYBY, REMARKS)
values ('API_WFM', 'WFM Server', 'API', 'A', 'Y', '81149189', '81149189', null);
INSERT INTO user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, ABSTRACT, CREATEBY, MODIFYBY, REMARKS)
VALUES ('API_BES', 'BES Server', 'API', 'A', 'Y', '81209181', '81209181', null);

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('O_CHL', 'CHL', 'OPT', 'A', '01634476', '01634476', null);
insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('TH__O_CHL', 'CHL Team Lead', 'TEAM_HEAD', 'A', '01634476', '01634476', null);

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('E_FIELD', 'FIELD', 'ENG', 'A', '01634476', '01634476', null);
insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('TH__E_FIELD', 'FIELD Team Lead', 'TEAM_HEAD', 'A', '01634476', '01634476', null);

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('E_ISRC_BB', 'ISRC BB', 'ENG', 'A', '01634476', '01634476', null);
insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('TH__E_ISRC_BB', 'ISRC BB Team Lead', 'TEAM_HEAD', 'A', '01634476', '01634476', null);

insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('E_ISRC_V', 'ISRC Voice', 'ENG', 'A', '01634476', '01634476', null);
insert into user_role (ROLE_ID, ROLE_DESC, PARENT_ROLE_ID, STATUS, CREATEBY, MODIFYBY, REMARKS)
values ('TH__E_ISRC_V', 'ISRC Voice Team Lead', 'TEAM_HEAD', 'A', '01634476', '01634476', null);
commit;