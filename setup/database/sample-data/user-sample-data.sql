-- Email User
insert into USER_PROFILE (USER_ID,NAME, STATUS, MOBILE, EMAIL, STAFF_ID, PASSWORD, PASSWORD_MODIFYDATE,CREATEBY, MODIFYBY)
values ('E1', 'Jason Tan', 'A', '28832299', 'jason.sp.tan@pccw.com', '81149189', '$2a$10$m/MsZJlDJs3MEy/zo4sab.CJZUCb4klpYL3glvMJb6zEoZID0Qlve', null, 'E1', 'E1');

-- LDAP User
insert into USER_PROFILE (USER_ID,NAME, STATUS, LDAP_DOMAIN,MOBILE, STAFF_ID,CREATEBY, MODIFYBY)
values ('81149189', 'Jason Tan', 'A','81149189@corphq.hk.pccw.com', '28832299','81149189', '81149189', '81149189');

-- update password to  "password"
-- update USER_PROFILE
-- set PASSWORD = '$2a$10$WqOnSBerBMjNSjC0JePQOuQUMQ3Gs2WQDilvMWCRD.oBWmxYnsZ.6'
-- where USER_ID = 'T20190905';