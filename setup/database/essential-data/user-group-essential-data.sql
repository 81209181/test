insert into NOC_USER_GROUP (GROUP_ID, GROUP_NAME, PARENT_GROUP, DESCRIPTION)
values ('ROOT', 'ROOT Group', null, '	Can config system');
insert into NOC_USER_GROUP (GROUP_ID, GROUP_NAME, PARENT_GROUP, DESCRIPTION)
values ('ADMIN', 'HKT Admin Group', 'ROOT', 'Can manage all users');
insert into NOC_USER_GROUP (GROUP_ID, GROUP_NAME, PARENT_GROUP, DESCRIPTION)
values ('USER', 'HKT User Group', 'ADMIN', 'Can handle daily operation');
insert into NOC_USER_GROUP (GROUP_ID, GROUP_NAME, PARENT_GROUP, DESCRIPTION)
values ('C_ADMIN', 'Client Admin Group', 'ADMIN', 'Can authorize anyone to access SkyExchange');
insert into NOC_USER_GROUP (GROUP_ID, GROUP_NAME, PARENT_GROUP, DESCRIPTION)
values ('C_USER', 'Client User Group', 'C_ADMIN', 'Can only authorize own self to access SkyExchange	');
