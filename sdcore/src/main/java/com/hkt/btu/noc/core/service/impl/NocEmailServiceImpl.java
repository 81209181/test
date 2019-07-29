package com.hkt.btu.noc.core.service.impl;


import com.hkt.btu.common.core.service.BtuTemplateEngineService;
import com.hkt.btu.noc.core.service.NocEmailService;
import com.hkt.btu.noc.core.service.NocSiteConfigService;
import com.hkt.btu.noc.core.service.bean.NocEmailBean;
import com.hkt.btu.noc.core.service.bean.NocSiteConfigBean;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.hkt.btu.noc.core.service.bean.NocEmailBean.*;


public class NocEmailServiceImpl implements NocEmailService {
    private static final Logger LOG = LogManager.getLogger(NocEmailServiceImpl.class);

    private static final String EMAIL_TEMPLATE_DIR = "email/";
    private static final String EMAIL_TEMPLATE_SUBJECT_SUFFIX = "_subject";
    private static final String EMAIL_TEMPLATE_BODY_SUFFIX = "_body";

    private InternetAddress senderProfile;


    @Resource(name = "templateEngineService")
    BtuTemplateEngineService btuTemplateEngineService;
    @Resource(name = "siteConfigService")
    NocSiteConfigService nocSiteConfigService;

    private JavaMailSender javaMailSender;

    private JavaMailSender getJavaMailSender() {
        if(javaMailSender==null){
            reload();
        }
        return javaMailSender;
    }


    @Override
    public void reload() {
        NocSiteConfigBean nocSiteConfigBean = nocSiteConfigService.getNocSiteConfigBean();

        JavaMailSenderImpl newJavaMailSender = new JavaMailSenderImpl();
        newJavaMailSender.setHost(nocSiteConfigBean.getMailHost());
        newJavaMailSender.setPort(nocSiteConfigBean.getMailPort());
        newJavaMailSender.setUsername(nocSiteConfigBean.getMailUsername());

        Properties props = newJavaMailSender.getJavaMailProperties();
        props.put("mail.debug", "true");
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.from", nocSiteConfigBean.getMailUsername());
        props.put("mail.transport.protocol", "smtp");

        // set send profile
        senderProfile = buildSenderProfile(nocSiteConfigBean);

        // swap in the new mail config
        javaMailSender = newJavaMailSender;

        LOG.info("NocEmailService reloaded.");
    }

    @Override
    public void send(String recipient, String subjectText, String bodyText) throws MessagingException {
        String templateId = NocEmailBean.DEFAULT_EMAIL.TEMPLATE_ID;

        // prepare email data
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(NocEmailBean.DEFAULT_EMAIL.EMAIL_SUBJECT, subjectText);
        dataMap.put(NocEmailBean.DEFAULT_EMAIL.EMAIL_BODY, bodyText);

        send(templateId, recipient, dataMap);
    }

    @Override
    public void send(String templateId, String recipient, Map<String, Object> dataMap) throws MessagingException {
        JavaMailSender mailSender = getJavaMailSender();
        NocSiteConfigBean nocSiteConfigBean = nocSiteConfigService.getNocSiteConfigBean();

        String subjectTemplate = EMAIL_TEMPLATE_DIR + templateId + EMAIL_TEMPLATE_SUBJECT_SUFFIX;
        String bodyTemplate = EMAIL_TEMPLATE_DIR + templateId + EMAIL_TEMPLATE_BODY_SUFFIX;

        // inject common data to dataMap
        injectGlobalEmailData(dataMap, nocSiteConfigBean);
        dataMap.put(EMAIL_BASIC_TEMPLATE_ID, templateId);
        dataMap.put(EMAIL_BASIC_RECIPIENT, recipient);

        // build html content
        String subjectString = btuTemplateEngineService.buildHtmlStringFromHtmlFile(subjectTemplate, dataMap);
        String htmlBodyString = btuTemplateEngineService.buildHtmlStringFromHtmlFile(bodyTemplate, dataMap);

        // add subject prefix to non-production email
        if( ! nocSiteConfigService.isProductionServer() ){
            subjectString = String.format("[%s] %s", nocSiteConfigBean.getServerType(), subjectString);
        }


        // build email object
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setTo(recipient);
        messageHelper.setSubject(subjectString);
        messageHelper.setText(htmlBodyString, true);
        if( senderProfile!=null ){
            messageHelper.setFrom( senderProfile );
        }

        // add inline img
        messageHelper.addInline(EMAIL_GLOBAL_LOGO_CID, EMAIL_GLOBAL_LOGO_RESOURCE, CONTENT_TYPE_PNG);
        messageHelper.addInline(EMAIL_GLOBAL_LOGO_HKT_CID, EMAIL_GLOBAL_LOGO_HKT_RESOURCE, CONTENT_TYPE_PNG);
        messageHelper.addInline(EMAIL_GLOBAL_LOGO_PCCW_GRP_CID, EMAIL_GLOBAL_LOGO_PCCW_GRP_RESOURCE, CONTENT_TYPE_PNG);

        // send email
        if(nocSiteConfigService.isDevelopmentServer()) {
            LOG.warn("Email Recipient: " + recipient);
            LOG.warn("Email Subject: " + subjectString);
            LOG.warn("Email Body: \n" + htmlBodyString);
            LOG.warn("CANNOT send above email in development environment.");
        } else {
            mailSender.send(message);
            LOG.info("Sent email [" + subjectString + "] to " + recipient + ".");
        }
    }

    @Override
    public void sendErrorStackTrace(String recipient, String subjectText, Exception e) throws MessagingException {
        String templateId = NocEmailBean.ERROR_STACK_TRACE_EMAIL.TEMPLATE_ID;

        // escape stacktrace
        String errorStackTraceString = ExceptionUtils.getStackTrace(e);
        errorStackTraceString = errorStackTraceString.replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
        errorStackTraceString = errorStackTraceString.replaceAll("(\t)", "&nbsp;&nbsp;&nbsp;&nbsp;");

        // prepare email data
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(NocEmailBean.ERROR_STACK_TRACE_EMAIL.EMAIL_SUBJECT, subjectText);
        dataMap.put(NocEmailBean.ERROR_STACK_TRACE_EMAIL.EMAIL_BODY, errorStackTraceString);

        send(templateId, recipient, dataMap);
    }

    private void injectGlobalEmailData(Map<String, Object> dataMap, NocSiteConfigBean nocSiteConfigBean) {
        if(dataMap==null){
            dataMap = new HashMap<>();
        }

        String webUrl = nocSiteConfigBean.getAppHttpsUrl();
        String webName = nocSiteConfigBean.getAppName();

        dataMap.put(EMAIL_GLOBAL_CONTACT_WEB_URL, webUrl);
        dataMap.put(EMAIL_GLOBAL_CONTACT_WEB_NAME, webName);
        dataMap.put(EMAIL_GLOBAL_LOGO_CID, EMAIL_GLOBAL_LOGO_CID);
        dataMap.put(EMAIL_GLOBAL_LOGO_HKT_CID, EMAIL_GLOBAL_LOGO_HKT_CID);
        dataMap.put(EMAIL_GLOBAL_LOGO_PCCW_GRP_CID, EMAIL_GLOBAL_LOGO_PCCW_GRP_CID);
    }

    private InternetAddress buildSenderProfile(NocSiteConfigBean nocSiteConfigBean){
        try {
            String fromAddress = nocSiteConfigBean.getMailUsername();
            String fromName = nocSiteConfigBean.getAppName();
            return new InternetAddress(fromAddress, fromName);
        } catch (UnsupportedEncodingException | NullPointerException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }
}
