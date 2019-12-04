-- Site Config
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'givenDomain', 'sd.pccw.com', 'String', sysdate, 0, sysdate, 0);
commit;
-- oldTable.API_AUTHORIZATION (for ref)
-- basic,bes,jsa9u3vhs1
-- basic,hktcloud,hktcloud20180515
-- basic,wfm,wfmwfm123


-- oldTable.SITE_INTERFACE (for ref)
-- TEST_LINE,http://10.252.16.151:8080,,,,,,,

-- ITSM_RESTFUL,https://itsm.pccw.com/cloudrs,sa,sa2018,,,,,
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_ITSM_RESTFUL', 'systemName', 'ITSM_RESTFUL', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_ITSM_RESTFUL', 'url', 'https://itsm.pccw.com/cloudrs', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_ITSM_RESTFUL', 'userName', 'sa', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_ITSM_RESTFUL', 'password', 'sa2018', 'String', current_timestamp, 0, current_timestamp, 0);
-- WFM,https://wfm.pccw.com/wfm,sd,Ki6/rEDs47*^5,,,,,
Insert into CONFIG_PARAM (CONFIG_GROUP,CONFIG_KEY,CONFIG_VALUE,CONFIG_VALUE_TYPE,CREATEDATE,CREATEBY,MODIFYDATE,MODIFYBY)
values ('API_WFM','url','https://wfm.pccw.com/wfm','String',current_timestamp,0,current_timestamp,0);
Insert into CONFIG_PARAM (CONFIG_GROUP,CONFIG_KEY,CONFIG_VALUE,CONFIG_VALUE_TYPE,CREATEDATE,CREATEBY,MODIFYDATE,MODIFYBY)
values ('API_WFM','userName','sd','String',current_timestamp,0,current_timestamp,0);
Insert into CONFIG_PARAM (CONFIG_GROUP,CONFIG_KEY,CONFIG_VALUE,CONFIG_VALUE_TYPE,CREATEDATE,CREATEBY,MODIFYDATE,MODIFYBY)
values ('API_WFM','password','Ki6/rEDs47*^5','String',current_timestamp,0,current_timestamp,0);
-- ITSM,https://itsm.pccw.com/itsm,,,,,,,
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_ITSM', 'systemName', 'ITSM', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_ITSM', 'url', 'https://itsm.pccw.com/itsm', 'String', current_timestamp, 0, current_timestamp, 0);
-- BES
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_BES', 'systemName', 'BES', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_BES', 'url', 'https://apicrmn.bpu.pccw.com', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_BES', 'userName', '999007', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_BES', 'password', 'input on page', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_BES', 'xAppKey', 'input on page', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_BES', 'beId', '101', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_BES', 'channelType', '613', 'String', current_timestamp, 0, current_timestamp, 0);
-- NORARS
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_NORARS', 'systemName', 'NORARS', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_NORARS', 'url', 'https://nora.oss.pccw.com/norars/', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_NORARS', 'userName', 'sd', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_NORARS', 'password', 'input on page', 'String', current_timestamp, 0, current_timestamp, 0);