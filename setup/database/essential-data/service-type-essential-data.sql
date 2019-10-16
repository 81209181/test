-- service type
insert into service_type(service_type_code,service_type_name,createby,modifyby)
values('ONE','One Communication','system','system');
insert into service_type(service_type_code,service_type_name,createby,modifyby)
values('E_CLOUD','Enterprise Cloud','system','system');
insert into service_type(service_type_code,service_type_name,createby,modifyby)
values('VOIP','voice VoIP','system','system');
insert into service_type(service_type_code,service_type_name,createby,modifyby)
values('BN','Broadaband','system','system');

-- ONE
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('ONE','OCCentrexCordless','system');
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('ONE','OCCloudOffice','system');
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('ONE','OCDDIGroup','system');
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('ONE','OCStandbyOrder','system');
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('ONE','OCVOIP','system');
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('ONE','Site Survey','system');
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('ONE','Broadband in EC','system');
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('ONE','OCIPTVoip','system');
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('ONE','OCOffice365License','system');
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('ONE','OCOneCommApp','system');
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('ONE','OCOneCommBundle','system');
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('ONE','OCCentrexVoip','system');
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('ONE','OCDomainReg','system');
-- E_CLOUD
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('E_CLOUD','Enterprise Cloud','system');
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('E_CLOUD','Enterprise Cloud (Others)','system');
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('E_CLOUD','VM Ware Primary','system');
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('E_CLOUD','O365 Primary','system');
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('E_CLOUD','Insync Primary','system');
-- VOIP
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('VOIP','Enterprise Centrex VoIP','system');
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('VOIP','VoIP Offering','system');
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('VOIP','VoIP for EC','system');
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('VOIP','VoIP for One Comm','system');
-- BN
insert into service_type_offer_mapping(service_type_code,offer_name,createby)
values('BN','broadband','system');