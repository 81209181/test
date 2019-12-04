-- User Role Path Ctrl
insert into USER_ROLE_PATH_CTRL (ROLE_ID, PATH_CTRL_ID, CREATEBY)
select 'BASIC', PATH_CTRL_ID, '01634476'
from PATH_CTRL
where PATH = '/user/**';

insert into USER_ROLE_PATH_CTRL (ROLE_ID, PATH_CTRL_ID, CREATEBY)
select 'SYS_ADMIN', PATH_CTRL_ID, '01634476'
from PATH_CTRL
where PATH = '/**';

insert into USER_ROLE_PATH_CTRL (ROLE_ID, PATH_CTRL_ID, CREATEBY)
select 'TEAM_HEAD', PATH_CTRL_ID, '01634476'
from PATH_CTRL
where PATH = '/admin/manage-user/**';

insert into USER_ROLE_PATH_CTRL (ROLE_ID, PATH_CTRL_ID, CREATEBY)
select 'OPT', PATH_CTRL_ID, '01634476'
from PATH_CTRL
where PATH = '/ticket/**';

insert into USER_ROLE_PATH_CTRL (ROLE_ID, PATH_CTRL_ID, CREATEBY)
select 'API_WFM', PATH_CTRL_ID, '01634476'
from PATH_CTRL
where PATH = '/wfm-api/**';

insert into USER_ROLE_PATH_CTRL (ROLE_ID, PATH_CTRL_ID, CREATEBY)
select 'API_BES', PATH_CTRL_ID, '01634476'
from PATH_CTRL
where PATH = '/bes-api/**';
commit;