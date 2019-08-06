package com.hkt.btu.sd.core.service.bean;

import org.springframework.core.io.ClassPathResource;

public class SdEmailBean {
    public static final String CONTENT_TYPE_PNG = "image/png";

    // global email data
    public static final String EMAIL_GLOBAL_LOGO_CID = "email_global_logo_cid";
    public static final ClassPathResource EMAIL_GLOBAL_LOGO_RESOURCE = new ClassPathResource("static/img/logo-site.png");
    public static final String EMAIL_GLOBAL_LOGO_HKT_CID = "email_global_logo_hkt_cid";
    public static final ClassPathResource EMAIL_GLOBAL_LOGO_HKT_RESOURCE = new ClassPathResource("static/img/logo-footer-hkt.png");
    public static final String EMAIL_GLOBAL_LOGO_PCCW_GRP_CID = "email_global_logo_pccw_grp_cid";
    public static final ClassPathResource EMAIL_GLOBAL_LOGO_PCCW_GRP_RESOURCE = new ClassPathResource("static/img/logo-footer-pccw.png");
    public static final String EMAIL_GLOBAL_CONTACT_WEB_URL = "email_global_web_url";
    public static final String EMAIL_GLOBAL_CONTACT_WEB_NAME = "email_global_web_name";

    // common email data
    public static final String EMAIL_BASIC_TEMPLATE_ID = "email_basic_template_id";
    public static final String EMAIL_BASIC_RECIPIENT = "email_basic_recipient";
    public static final String EMAIL_BASIC_RECIPIENT_NAME = "email_basic_recipient_name";

    // default email
    public class DEFAULT_EMAIL{
        public static final String TEMPLATE_ID = "defaultEmail";
        public static final String EMAIL_SUBJECT = "default_email_subject";
        public static final String EMAIL_BODY = "default_email_body";
    }

    // default email
    public class ERROR_STACK_TRACE_EMAIL{
        public static final String TEMPLATE_ID = "errorStackTraceEmail";
        public static final String EMAIL_SUBJECT = "default_email_subject";
        public static final String EMAIL_BODY = "default_email_body";
    }

    // init password email
    public class INIT_PW_EMAIL{
        public static final String TEMPLATE_ID = "initPasswordEmail";
        public static final String OTP = "otp";
    }

    // reset password email
    public class RESET_PW_EMAIL{
        public static final String TEMPLATE_ID = "resetPasswordEmail";
        public static final String OTP = "otp";
    }

    // access request customer confirmation email
    public class ACCESS_REQUEST_CFM_CUST_EMAIL{
        public static final String TEMPLATE_ID = "accessRequestConfirmCustEmail";
        public static final String TICKET_NUM = "ticketNum";
        public static final String REQUESTOR_NAME = "requestorName";
        public static final String VISIT_LOC = "visitLoc";
        public static final String VISIT_DATE = "visitDate";
        public static final String VISIT_TIME = "visitTime";
        public static final String VISITOR_COUNT = "visitorCount";
    }

    // access request NFM confirmation email
    public class ACCESS_REQUEST_CFM_NFM_EMAIL{
        public static final String TEMPLATE_ID = "accessRequestConfirmNfmEmail";
        public static final String TICKET_NUM = "ticketNum";
        public static final String REQUESTOR_NAME = "requestorName";
        public static final String REQUESTOR_USER_ID = "requestorUserId";
        public static final String REQUESTOR_COMPANY = "requestorCompany";
        public static final String VISIT_LOC = "visitLoc";
        public static final String VISIT_DATE = "visitDate";
        public static final String VISIT_TIME = "visitTime";
        public static final String VISITOR_COUNT = "visitorCount";
    }


}
