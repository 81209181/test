-- SQL_REPORT
INSERT INTO SD.SQL_REPORT (REPORT_ID, REPORT_NAME, CRON_EXP, STATUS, SQL, EXPORT_TO, EMAIL_TO, CREATEDATE, CREATEBY, MODIFYDATE, MODIFYBY, REMARKS)
VALUES (1, 'test', '0/20 * * * * ?  ', 'A', 'SELECT * FROM USER_PROFILE', 'D:\', null, TO_DATE('2019-09-09 06:01:46', 'YYYY-MM-DD HH24:MI:SS'), '81149189', TO_DATE('2019-09-09 06:01:53', 'YYYY-MM-DD HH24:MI:SS'), '81149189', null);