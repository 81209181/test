-- Site Config
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'givenDomain', 'sd.pccw.com', 'String', sysdate, 0, sysdate, 0);
commit;
-- oldTable.API_AUTHORIZATION (for ref)
-- basic,bes,jsa9u3vhs1
-- basic,hktcloud,hktcloud20180515
-- basic,wfm,wfmwfm123


-- oldTable.SITE_INTERFACE (for ref)
-- ITSM_RESTFUL,https://itsm.pccw.com/cloudrs,sa,sa2018,,,,,
-- WFM,https://wfm.pccw.com/wfm,sd,Ki6/rEDs47*^5,,,,,
-- TEST_LINE,http://10.252.16.151:8080,,,,,,,
-- ITSM,https://itsm.pccw.com/itsm,,,,,,,
