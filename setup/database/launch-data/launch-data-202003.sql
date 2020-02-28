-- pre-launch
insert into service_type(service_type_code,service_type_name,createby,modifyby)
values('METER','Smart Meter','system','system');


INSERT INTO SYMPTOM (SYMPTOM_CODE, SYMPTOM_DESCRIPTION, SYMPTOM_GROUP_CODE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY) VALUES ('CL005', 'Need Field', '105', SYSDATE, '01634476', SYSDATE, '01634476');
INSERT INTO SYMPTOM (SYMPTOM_CODE, SYMPTOM_DESCRIPTION, SYMPTOM_GROUP_CODE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY) VALUES ('CL006', 'Need PND', '105', SYSDATE, '01634476', SYSDATE, '01634476');
INSERT INTO SYMPTOM (SYMPTOM_CODE, SYMPTOM_DESCRIPTION, SYMPTOM_GROUP_CODE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY) VALUES ('CL007', 'Need Field and PND', '105', SYSDATE, '01634476', SYSDATE, '01634476');
INSERT INTO SYMPTOM (SYMPTOM_CODE, SYMPTOM_DESCRIPTION, SYMPTOM_GROUP_CODE, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY) VALUES ('CL008', 'Need PND and Field', '105', SYSDATE, '01634476', SYSDATE, '01634476');

INSERT INTO SYMPTOM_MAPPING (SERVICE_TYPE_CODE, SYMPTOM_CODE, CREATEDATE, CREATEBY) VALUES ('METER', 'CL005', SYSDATE, '01634476');
INSERT INTO SYMPTOM_MAPPING (SERVICE_TYPE_CODE, SYMPTOM_CODE, CREATEDATE, CREATEBY) VALUES ('METER', 'CL006', SYSDATE, '01634476');
INSERT INTO SYMPTOM_MAPPING (SERVICE_TYPE_CODE, SYMPTOM_CODE, CREATEDATE, CREATEBY) VALUES ('METER', 'CL007', SYSDATE, '01634476');
INSERT INTO SYMPTOM_MAPPING (SERVICE_TYPE_CODE, SYMPTOM_CODE, CREATEDATE, CREATEBY) VALUES ('METER', 'CL008', SYSDATE, '01634476');


-- post-launch
