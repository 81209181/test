# noinspection SpellCheckingInspectionForFile

# Company
insert into NOC_COMPANY (COMPANY_ID, NAME, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
VALUES (1, 'Hong Kong Telecommunications (HKT) Limited', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);
insert into NOC_COMPANY (COMPANY_ID, NAME, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
VALUES (2, 'Client Company', CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0);

# User
# BCryptPasswordEncoder, BCrypt round: 10 (spring security default)
insert into NOC_USER (USER_ID, NAME, STATUS, MOBILE, EMAIL, COMPANY_ID, STAFF_ID, LDAP_DOMAIN, PASSWORD, PASSWORD_MODIFYDATE, CREATEBY, MODIFYBY, REMARKS)
values (1, 'Jason Kong', 'A', null, 'jason.yh.kong@pccw.com', 1, null, null, '$2a$10$WqOnSBerBMjNSjC0JePQOuQUMQ3Gs2WQDilvMWCRD.oBWmxYnsZ.6', null, 0, 0, 'password: password' );
insert into NOC_USER_USER_GROUP(USER_ID, GROUP_ID, CREATEBY) values (1, 'ROOT', 0);
insert into NOC_USER_USER_GROUP(USER_ID, GROUP_ID, CREATEBY) values (1, 'ADMIN', 0);
insert into NOC_USER_USER_GROUP(USER_ID, GROUP_ID, CREATEBY) values (1, 'USER', 0);
insert into NOC_USER_USER_GROUP(USER_ID, GROUP_ID, CREATEBY) values (1, 'C_ADMIN', 0);
insert into NOC_USER_USER_GROUP(USER_ID, GROUP_ID, CREATEBY) values (1, 'C_USER', 0);

insert into NOC_USER (USER_ID, NAME, STATUS, MOBILE, EMAIL, COMPANY_ID, STAFF_ID, LDAP_DOMAIN, PASSWORD, PASSWORD_MODIFYDATE, CREATEBY, MODIFYBY, REMARKS)
values (5, 'admin1', 'A', null, 'admin1@pccw.com', 1, null, null, '$2a$10$WqOnSBerBMjNSjC0JePQOuQUMQ3Gs2WQDilvMWCRD.oBWmxYnsZ.6', CURRENT_TIMESTAMP, 0, 0, 'password: password' );
insert into NOC_USER_USER_GROUP(USER_ID, GROUP_ID, CREATEBY) values (5, 'ADMIN', 0);
insert into NOC_USER_USER_GROUP(USER_ID, GROUP_ID, CREATEBY) values (5, 'USER', 0);
insert into NOC_USER_USER_GROUP(USER_ID, GROUP_ID, CREATEBY) values (5, 'C_ADMIN', 0);
insert into NOC_USER_USER_GROUP(USER_ID, GROUP_ID, CREATEBY) values (5, 'C_USER', 0);

insert into NOC_USER (USER_ID, NAME, STATUS, MOBILE, EMAIL, COMPANY_ID, STAFF_ID, LDAP_DOMAIN, PASSWORD, PASSWORD_MODIFYDATE, CREATEBY, MODIFYBY, REMARKS)
values (2, 'user1', 'A', null, 'user1@pccw.com', 1, null, null, '$2a$10$WqOnSBerBMjNSjC0JePQOuQUMQ3Gs2WQDilvMWCRD.oBWmxYnsZ.6', CURRENT_TIMESTAMP, 0, 0, 'password: password' );
insert into NOC_USER_USER_GROUP(USER_ID, GROUP_ID, CREATEBY) values (2, 'USER', 0);

insert into NOC_USER (USER_ID, NAME, STATUS, MOBILE, EMAIL, COMPANY_ID, STAFF_ID, LDAP_DOMAIN, PASSWORD, PASSWORD_MODIFYDATE, CREATEBY, MODIFYBY, REMARKS)
values (3, 'cadmin1', 'A', null, 'cadmin1@pccw.com', 2, null, null, '$2a$10$WqOnSBerBMjNSjC0JePQOuQUMQ3Gs2WQDilvMWCRD.oBWmxYnsZ.6', CURRENT_TIMESTAMP, 0, 0, 'password: password' );
insert into NOC_USER_USER_GROUP(USER_ID, GROUP_ID, CREATEBY) values (3, 'C_ADMIN', 0);

insert into NOC_USER (USER_ID, NAME, STATUS, MOBILE, EMAIL, COMPANY_ID, STAFF_ID, LDAP_DOMAIN, PASSWORD, PASSWORD_MODIFYDATE, CREATEBY, MODIFYBY, REMARKS)
values (4, 'cuser1', 'A', null, 'cuser1@pccw.com', 2, null, null, '$2a$10$WqOnSBerBMjNSjC0JePQOuQUMQ3Gs2WQDilvMWCRD.oBWmxYnsZ.6', CURRENT_TIMESTAMP, 0, 0, 'password: password' );
insert into NOC_USER_USER_GROUP(USER_ID, GROUP_ID, CREATEBY) values (4, 'C_USER', 0);

