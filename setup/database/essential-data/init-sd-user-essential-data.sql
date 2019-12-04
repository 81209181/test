insert into USER_PROFILE (USER_ID, NAME, STATUS, MOBILE, EMAIL, LDAP_DOMAIN, CREATEBY, MODIFYBY, DOMAIN_EMAIL, PRIMARY_ROLE_ID)
values ('01634476', 'Jason Kong', 'A', '28833518', 'jason.yh.kong@pccw.com', '@corphq.hk.pccw.com','01634476','01634476','jason.yh.kong@pccw.com', 'O_CHL');

insert into USER_USER_ROLE (USER_ID, ROLE_ID) values ('01634476','O_CHL');
insert into USER_USER_ROLE (USER_ID, ROLE_ID) values ('01634476','TH__O_CHL');
insert into USER_USER_ROLE (USER_ID, ROLE_ID) values ('01634476','SYS_ADMIN');

commit;