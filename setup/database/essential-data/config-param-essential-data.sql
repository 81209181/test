-- Site Config
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'passwordResetOtpLifespanInMin', '30', 'Integer', current_timestamp, 0, current_timestamp, 0);

insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'appName', 'ServiceDesk', 'String', current_timestamp, 0, current_timestamp, 0);

insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'givenDomain', '', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'cronjobHostname', 'yecvm_saapp01', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'prodHostname', 'yecvm_saapp01', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'prodStandbyHostname', 'jecvm_saapp01', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'uatHostname', 'yecdevvm-sauat01', 'String', current_timestamp, 0, current_timestamp, 0);

insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'mailHost', 'smtp.pccw.com', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'mailPort', '25', 'Integer', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'mailUsername', 'sdadmin@pccw.com', 'String', current_timestamp, 0, current_timestamp, 0);

insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('BES', 'systemName', 'BES', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('BES', 'url', 'tba', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('BES', 'userName', 'tba', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('BES', 'password', 'tba', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('BES', 'xAppKey', 'tba', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('BES', 'beId', 'tba', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('BES', 'channelType', 'tba', 'String', current_timestamp, 0, current_timestamp, 0);

insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('ITSM_RESTFUL', 'systemName', 'ITSM_RESTFUL', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('ITSM_RESTFUL', 'url', 'tba', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('ITSM_RESTFUL', 'userName', 'tba', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('ITSM_RESTFUL', 'password', 'tba', 'String', current_timestamp, 0, current_timestamp, 0);

insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('ITSM', 'systemName', 'ITSM', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('ITSM', 'url', 'tba', 'String', current_timestamp, 0, current_timestamp, 0);

insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('NORARS', 'systemName', 'NORARS', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('NORARS', 'url', 'tba', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('NORARS', 'userName', 'tba', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('NORARS', 'password', 'tba', 'String', current_timestamp, 0, current_timestamp, 0);

-- Cronjob Team
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('CRONJOB', 'errorEmail', 'jason.yh.kong@pccw.com', 'String', current_timestamp, 0, current_timestamp, 0);