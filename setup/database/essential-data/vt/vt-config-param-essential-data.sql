-- Site Config
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'givenDomain', 'yecvm-savt01.pccw.com', 'String', sysdate, 0, sysdate, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'cronjobHostname', 'yecvm-savt01', 'String', current_timestamp, 0, current_timestamp, 0);



insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_BES', 'systemName', 'BES', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_BES', 'url', 'https://10.50.68.110:1443', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_BES', 'header.Authorization', 'Bearer fcb98614187cf228da1d844e787b28dd', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_BES', 'header.OperatorId', '999007', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_BES', 'header.X-APP-Key', 'b0025e19d6a649278cdfeb520c224ef2', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_BES', 'header.BeId', '101', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_BES', 'header.ChannelType', '613', 'String', current_timestamp, 0, current_timestamp, 0);

insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_ITSM_RESTFUL', 'systemName', 'ITSM_RESTFUL', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_ITSM_RESTFUL', 'url', 'https://jecvm-itsmvt01.pccw.com/cloudrs', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_ITSM_RESTFUL', 'userName', 'sa', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_ITSM_RESTFUL', 'password', 'sa2018', 'String', current_timestamp, 0, current_timestamp, 0);

insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_ITSM', 'systemName', 'ITSM', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_ITSM', 'url', 'https://jecvm-itsmvt01.pccw.com/itsm', 'String', current_timestamp, 0, current_timestamp, 0);

insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_NORARS', 'systemName', 'NORARS', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_NORARS', 'url', 'https://10.252.15.139', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_NORARS', 'userName', 'sd', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_NORARS', 'password', 'g$Hw0#MG-3', 'String', current_timestamp, 0, current_timestamp, 0);

insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_WFM', 'systemName', 'WFM', 'String', current_timestamp, 0, current_timestamp, 0);
Insert into CONFIG_PARAM (CONFIG_GROUP,CONFIG_KEY,CONFIG_VALUE,CONFIG_VALUE_TYPE,CREATEDATE,CREATEBY,MODIFYDATE,MODIFYBY)
values ('API_WFM','url','https://yecvm-wfmvt01.pccw.com/wfm','String',current_timestamp,0,current_timestamp,0);
Insert into CONFIG_PARAM (CONFIG_GROUP,CONFIG_KEY,CONFIG_VALUE,CONFIG_VALUE_TYPE,CREATEDATE,CREATEBY,MODIFYDATE,MODIFYBY)
values ('API_WFM','userName','sd','String', current_timestamp, 0, current_timestamp, 0);
Insert into CONFIG_PARAM (CONFIG_GROUP,CONFIG_KEY,CONFIG_VALUE,CONFIG_VALUE_TYPE,CREATEDATE,CREATEBY,MODIFYDATE,MODIFYBY)
values ('API_WFM','password','Ki6/rEDs47*^5','String', current_timestamp, 0, current_timestamp, 0);

insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_UT_CALL', 'systemName', 'UT_CALL', 'String', current_timestamp, 0, current_timestamp, 0);
Insert into CONFIG_PARAM (CONFIG_GROUP,CONFIG_KEY,CONFIG_VALUE,CONFIG_VALUE_TYPE,CREATEDATE,CREATEBY,MODIFYDATE,MODIFYBY)
values ('API_UT_CALL','url','http://10.252.16.151:8080','String',current_timestamp,0,current_timestamp,0);
Insert into CONFIG_PARAM (CONFIG_GROUP,CONFIG_KEY,CONFIG_VALUE,CONFIG_VALUE_TYPE,CREATEDATE,CREATEBY,MODIFYDATE,MODIFYBY)
values ('API_UT_CALL','fid','f023000000','String', current_timestamp, 0, current_timestamp, 0);
Insert into CONFIG_PARAM (CONFIG_GROUP,CONFIG_KEY,CONFIG_VALUE,CONFIG_VALUE_TYPE,CREATEDATE,CREATEBY,MODIFYDATE,MODIFYBY)
values ('API_UT_CALL','user','SERVDESK','String', current_timestamp, 0, current_timestamp, 0);
Insert into CONFIG_PARAM (CONFIG_GROUP,CONFIG_KEY,CONFIG_VALUE,CONFIG_VALUE_TYPE,CREATEDATE,CREATEBY,MODIFYDATE,MODIFYBY)
values ('API_UT_CALL','pwd','nvng[kGZE\C^','String', current_timestamp, 0, current_timestamp, 0);
Insert into CONFIG_PARAM (CONFIG_GROUP,CONFIG_KEY,CONFIG_VALUE,CONFIG_VALUE_TYPE,CREATEDATE,CREATEBY,MODIFYDATE,MODIFYBY)
values ('API_UT_CALL','sys','servdesk','String', current_timestamp, 0, current_timestamp, 0);
Insert into CONFIG_PARAM (CONFIG_GROUP,CONFIG_KEY,CONFIG_VALUE,CONFIG_VALUE_TYPE,CREATEDATE,CREATEBY,MODIFYDATE,MODIFYBY)
values ('API_UT_CALL','seq','2','String', current_timestamp, 0, current_timestamp, 0);
Insert into CONFIG_PARAM (CONFIG_GROUP,CONFIG_KEY,CONFIG_VALUE,CONFIG_VALUE_TYPE,CREATEDATE,CREATEBY,MODIFYDATE,MODIFYBY)
values ('API_UT_CALL','type','v1','String', current_timestamp, 0, current_timestamp, 0);


insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('API_OSS', 'systemName', 'OSS', 'String', sysdate, 0, sysdate, 0);
Insert into CONFIG_PARAM (CONFIG_GROUP,CONFIG_KEY,CONFIG_VALUE,CONFIG_VALUE_TYPE,CREATEDATE,CREATEBY,MODIFYDATE,MODIFYBY)
values ('API_WFM', 'url', 'http://10.6.52.130:58080', 'String', sysdate, 0, sysdate, 0);
