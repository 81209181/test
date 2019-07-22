# AntPath mapping control
# Implementation: BtuSecurityMetadataSource.java - buildResourceMapFromDb()
insert into PATH_CTRL (PATH_CTRL_ID, ANT_PATH, STATUS, CREATEBY, MODIFYBY) values (1, '/**', 'A', 0, 0);
insert into PATH_CTRL (PATH_CTRL_ID, ANT_PATH, STATUS, CREATEBY, MODIFYBY) values (2, '/admin/**', 'A', 0, 0);
insert into PATH_CTRL (PATH_CTRL_ID, ANT_PATH, STATUS, CREATEBY, MODIFYBY) values (3, '/user/**', 'A', 0, 0);
insert into PATH_CTRL (PATH_CTRL_ID, ANT_PATH, STATUS, CREATEBY, MODIFYBY) values (4, '/admin/manage-group/**', 'A', 0, 0);
insert into PATH_CTRL (PATH_CTRL_ID, ANT_PATH, STATUS, CREATEBY, MODIFYBY) values (5, '/admin/manage-companyName/**', 'A', 0, 0);


# path and user group relation
insert into NOC_USER_GROUP_PATH_CTRL (GROUP_ID, PATH_CTRL_ID, CREATEBY) values ('ROOT', 1, 0);
insert into NOC_USER_GROUP_PATH_CTRL (GROUP_ID, PATH_CTRL_ID, CREATEBY) values ('ADMIN', 2, 0);
insert into NOC_USER_GROUP_PATH_CTRL (GROUP_ID, PATH_CTRL_ID, CREATEBY) values ('ADMIN', 3, 0);
insert into NOC_USER_GROUP_PATH_CTRL (GROUP_ID, PATH_CTRL_ID, CREATEBY) values ('ADMIN', 4, 0);
insert into NOC_USER_GROUP_PATH_CTRL (GROUP_ID, PATH_CTRL_ID, CREATEBY) values ('ADMIN', 5, 0);
insert into NOC_USER_GROUP_PATH_CTRL (GROUP_ID, PATH_CTRL_ID, CREATEBY) values ('USER', 3, 0);
insert into NOC_USER_GROUP_PATH_CTRL (GROUP_ID, PATH_CTRL_ID, CREATEBY) values ('C_ADMIN', 2, 0);
insert into NOC_USER_GROUP_PATH_CTRL (GROUP_ID, PATH_CTRL_ID, CREATEBY) values ('C_ADMIN', 3, 0);
insert into NOC_USER_GROUP_PATH_CTRL (GROUP_ID, PATH_CTRL_ID, CREATEBY) values ('C_USER', 3, 0);