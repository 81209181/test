-- Site Config
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'givenDomain', 'yecvm-sasit01.pccw.com', 'String', sysdate, 0, sysdate, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'cronjobHostname', 'yecvm-sasit01', 'String', current_timestamp, 0, current_timestamp, 0);

-- oldTable.API_AUTHORIZATION (for ref)
-- basic,bes,jsa9u3vhs1
-- basic,hktcloud,hktcloud20180515
-- basic,wfm,wfmwfm123

-- oldTable.SITE_INTERFACE (for ref)
-- TEST_LINE,http://10.252.16.151:8080,,,,,,,

-- BES,https://10.50.36.110:2443,999007,Bearer 5b4bf68e5dae6376ac9d569b31b09800,2019-05-10 11:01:40,01634476,16fdcfc5512645ebab925357499928ff,101,613
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_BES', 'systemName', 'BES', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_BES', 'url', 'https://10.50.68.110:2443', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_BES', 'userName', '999007', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_BES', 'password', 'Bearer 6947e99956c092e49764dee6c36f1a55', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_BES', 'xAppKey', '16fdcfc5512645ebab925357499928ff', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_BES', 'beId', '101', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_BES', 'channelType', '613', 'String', current_timestamp, 0, current_timestamp, 0);
-- NORARS,https://10.252.15.139,sd,g$Hw0#MG-3,2019-08-07 12:02:36,01634476,,,
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_NORARS', 'systemName', 'NORARS', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_NORARS', 'url', 'https://10.252.15.139', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_NORARS', 'userName', 'sd', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_NORARS', 'password', 'g$Hw0#MG-3', 'String', current_timestamp, 0, current_timestamp, 0);
-- ITSM_RESTFUL,https://10.111.7.32,ssa,sa2018,,,,,
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_ITSM_RESTFUL', 'systemName', 'ITSM_RESTFUL', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_ITSM_RESTFUL', 'url', 'https://jecvm-itsmsit01.pccw.com/cloudrs', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_ITSM_RESTFUL', 'userName', 'ssa', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_ITSM_RESTFUL', 'password', 'sa2018', 'String', current_timestamp, 0, current_timestamp, 0);
-- ITSM,https://10.111.7.32/itsm,,,,,,,
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_ITSM', 'systemName', 'ITSM', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_ITSM', 'url', 'https://jecvm-itsmsit01.pccw.com/itsm', 'String', current_timestamp, 0, current_timestamp, 0);
-- WFM,https://10.111.6.34/wfm,sd,Ki6/rEDs47*^5,,,,,
Insert into CONFIG_PARAM (CONFIG_GROUP,CONFIG_KEY,CONFIG_VALUE,CONFIG_VALUE_TYPE,CREATEDATE,CREATEBY,MODIFYDATE,MODIFYBY)
values ('API_WFM','url','https://yecvm-wfmsit01.pccw.com/wfm','String',current_timestamp,0,current_timestamp,0);
Insert into CONFIG_PARAM (CONFIG_GROUP,CONFIG_KEY,CONFIG_VALUE,CONFIG_VALUE_TYPE,CREATEDATE,CREATEBY,MODIFYDATE,MODIFYBY)
values ('API_WFM','userName','sd','String',current_timestamp,0,current_timestamp,0);
Insert into CONFIG_PARAM (CONFIG_GROUP,CONFIG_KEY,CONFIG_VALUE,CONFIG_VALUE_TYPE,CREATEDATE,CREATEBY,MODIFYDATE,MODIFYBY)
values ('API_WFM','password','Ki6/rEDs47*^5','String',current_timestamp,0,current_timestamp,0);