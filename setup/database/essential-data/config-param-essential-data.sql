-- Site Config
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'passwordResetOtpLifespanInMin', '30', 'Integer', current_timestamp, 0, current_timestamp, 0);

insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'appName', 'ServiceDesk', 'String', current_timestamp, 0, current_timestamp, 0);


insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'prodHostname', 'yecvm-saapp01', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'prodStandbyHostname', 'jecvm-saapp01', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'uatHostname', 'yecdevvm-sauat01', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'sitHostname', 'yecdevvm-sasit01', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'vtHostname', 'yecdevvm-savt01', 'String', current_timestamp, 0, current_timestamp, 0);

insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'mailHost', 'smtp.pccw.com', 'String', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'mailPort', '25', 'Integer', current_timestamp, 0, current_timestamp, 0);
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('SITE', 'mailUsername', 'sdadmin@pccw.com', 'String', current_timestamp, 0, current_timestamp, 0);

-- Cronjob Team
insert into CONFIG_PARAM(CONFIG_GROUP, CONFIG_KEY, CONFIG_VALUE, CONFIG_VALUE_TYPE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY)
values ('CRONJOB', 'errorEmail', 'jason.yh.kong@pccw.com', 'String', current_timestamp, 0, current_timestamp, 0);
commit;