-- AntPath mapping control
insert into PATH_CTRL(PATH, STATUS, DESCRIPTION,CREATEBY, MODIFYBY) values('/user/**', 'A', 'Basic Path', '81149189', '81149189');
insert into PATH_CTRL(PATH, STATUS, DESCRIPTION,CREATEBY, MODIFYBY) values('/**', 'A', 'System Admin', '81149189', '81149189');
insert into PATH_CTRL(PATH, STATUS, DESCRIPTION,CREATEBY, MODIFYBY) values('/ticket/**', 'A', 'Ticket', '81149189', '81149189');
insert into PATH_CTRL(PATH, STATUS, DESCRIPTION,CREATEBY, MODIFYBY) values('/symptom/**', 'A', 'Symptom', '81149189', '81149189');
insert into PATH_CTRL(PATH, STATUS, DESCRIPTION,CREATEBY, MODIFYBY) values('/admin/manage-user/**', 'A', 'Manage User', '81149189', '81149189');
insert into PATH_CTRL(PATH, STATUS, DESCRIPTION,CREATEBY, MODIFYBY) values('/admin/manage-role/**', 'A', 'Manage User Role', '81149189', '81149189');
insert into PATH_CTRL(PATH, STATUS, DESCRIPTION,CREATEBY, MODIFYBY) values('/wfm-api/**', 'A', 'WFM API', '01634476', '01634476');
insert into PATH_CTRL(PATH, STATUS, DESCRIPTION,CREATEBY, MODIFYBY) values('/bes-api/**', 'A', 'BES API', '01634476', '01634476');
commit;
