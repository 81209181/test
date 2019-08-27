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
values ('BES', 'url', 'https://10.50.36.110:2443', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('BES', 'userName', '999007', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('BES', 'password', 'tba', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('BES', 'xAppKey', '16fdcfc5512645ebab925357499928ff', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('BES', 'beId', '101', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('BES', 'channelType', '613', 'String', current_timestamp, 0, current_timestamp, 0);

insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('ITSM_RESTFUL', 'systemName', 'ITSM_RESTFUL', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('ITSM_RESTFUL', 'url', 'https://10.111.7.32', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('ITSM_RESTFUL', 'userName', 'ssa', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('ITSM_RESTFUL', 'password', 'tba', 'String', current_timestamp, 0, current_timestamp, 0);

insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('ITSM', 'systemName', 'ITSM', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('ITSM', 'url', 'https://10.111.7.32/itsm', 'String', current_timestamp, 0, current_timestamp, 0);

insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('NORARS', 'systemName', 'NORARS', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('NORARS', 'url', 'https://10.252.15.139', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('NORARS', 'userName', 'sd', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('NORARS', 'password', 'tba', 'String', current_timestamp, 0, current_timestamp, 0);

-- Cronjob Team
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('CRONJOB', 'errorEmail', 'jason.yh.kong@pccw.com', 'String', current_timestamp, 0, current_timestamp, 0);