-- WFM API Client
INSERT INTO USER_PROFILE (USER_ID, NAME, STATUS, MOBILE, EMAIL, LDAP_DOMAIN, PASSWORD, PASSWORD_MODIFYDATE, LOGIN_TRIED, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY, REMARKS, STAFF_ID, DOMAIN_EMAIL) VALUES ('WFM', 'WFM', 'A', '28832299', null, null, '$2a$10$0AXrHSEEF0.iFS91dk1OEu5Sxm48Gsmm/cB/nv3jsC1zZuO63HDjy', null, 0, SYSDATE, '81209181', SYSDATE, '81209181', null, null, null);
INSERT INTO USER_USER_ROLE (USER_ID, ROLE_ID) VALUES ('WFM', 'API_WFM');
-- INSERT INTO SD.USER_USER_ROLE (USER_ID, ROLE_ID) VALUES ('WFM', 'O_TEAM_B');

-- BES API Client
INSERT INTO USER_PROFILE (USER_ID, NAME, STATUS, MOBILE, EMAIL, LDAP_DOMAIN, PASSWORD, PASSWORD_MODIFYDATE, LOGIN_TRIED, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY, REMARKS, STAFF_ID, DOMAIN_EMAIL) VALUES ('BES', 'BES', 'A', '28832299', null, null, '$2a$10$0AXrHSEEF0.iFS91dk1OEu5Sxm48Gsmm/cB/nv3jsC1zZuO63HDjy', null, 0, SYSDATE, '81209181', SYSDATE, '81209181', null, null, null);
INSERT INTO USER_USER_ROLE (USER_ID, ROLE_ID) VALUES ('BES', 'API_BES');