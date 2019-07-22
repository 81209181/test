insert into CRON_JOB(JOB_GROUP, JOB_NAME, JOB_CLASS, CRON_EXP, STATUS, MANDATORY, CREATEBY, MODIFYBY)
values ('AccessRequest', 'NocAutoCpAccessRequestJob', 'com.hkt.btu.noc.core.job.NocAutoCpAccessRequestJob', 
        '0 0 1 ? * * *', 'A', 'N', 0, 0);

insert into CRON_JOB(JOB_GROUP, JOB_NAME, JOB_CLASS, CRON_EXP, STATUS, MANDATORY, CREATEBY, MODIFYBY)
values ('AccessRequest', 'NocGenerateAccessRequestHashedIdJob', 'com.hkt.btu.noc.core.job.NocGenerateAccessRequestHashedIdJob', 
        '0 15 0/4 ? * * *', 'A', 'Y', 0, 0);

