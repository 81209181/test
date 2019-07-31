package com.hkt.btu.sd.core.service.impl;


import com.hkt.btu.common.core.service.BtuTemplateEngineService;
import com.hkt.btu.sd.core.service.SdEmailService;
import com.hkt.btu.sd.core.service.SdSiteConfigService;
import com.hkt.btu.sd.core.service.bean.SdEmailBean;
import com.hkt.btu.sd.core.service.bean.SdSiteConfigBean;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.hkt.btu.sd.core.service.bean.SdEmailBean.*;

public class SdEmailServiceImpl implements SdEmailService {
    private static final Logger LOG = LogManager.getLogger(SdEmailServiceImpl.class);

    private static final String EMAIL_TEMPLATE_DIR = "email/";
    private static final String EMAIL_TEMPLATE_SUBJECT_SUFFIX = "_subject";
    private static final String EMAIL_TEMPLATE_BODY_SUFFIX = "_body";

    private InternetAddress senderProfile;


    @Resource(name = "templateEngineService")
    BtuTemplateEngineService btuTemplateEngineService;
    @Resource(name = "siteConfigService")
    SdSiteConfigService sdSiteConfigService;

    private JavaMailSender javaMailSender;

    private JavaMailSender getJavaMailSender() {
        if(javaMailSender==null){
            reload();
        }
        return javaMailSender;
    }


    @Override
    public void reload() {
        SdSiteConfigBean sdSiteConfigBean = sdSiteConfigService.getSdSiteConfigBean();

        JavaMailSenderImpl newJavaMailSender = new JavaMailSenderImpl();
        newJavaMailSender.setHost(sdSiteConfigBean.getMailHost());
        newJavaMailSender.setPort(sdSiteConfigBean.getMailPort());
        newJavaMailSender.setUsername(sdSiteConfigBean.getMailUsername());

        Properties props = newJavaMailSender.getJavaMailProperties();
        props.put("mail.debug", "true");
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.from", sdSiteConfigBean.getMailUsername());
        props.put("mail.transport.protocol", "smtp");

        // set send profile
        senderProfile = buildSenderProfile(sdSiteConfigBean);

        // swap in the new mail config
        javaMailSender = newJavaMailSender;

        LOG.info("SdEmailService reloaded.");
    }

    @Override
    public void send(String recipient, String subjectText, String bodyText) throws MessagingException {
        String templateId = SdEmailBean.DEFAULT_EMAIL.TEMPLATE_ID;

        // prepare email data
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(SdEmailBean.DEFAULT_EMAIL.EMAIL_SUBJECT, subjectText);
        dataMap.put(SdEmailBean.DEFAULT_EMAIL.EMAIL_BODY, bodyText);

        send(templateId, recipient, dataMap);
    }

    @Override
    public void send(String templateId, String recipient, Map<String, Object> dataMap) throws MessagingException {
        JavaMailSender mailSender = getJavaMailSender();
        SdSiteConfigBean sdSiteConfigBean = sdSiteConfigService.getSdSiteConfigBean();

        String subjectTemplate = EMAIL_TEMPLATE_DIR + templateId + EMAIL_TEMPLATE_SUBJECT_SUFFIX;
        String bodyTemplate = EMAIL_TEMPLATE_DIR + templateId + EMAIL_TEMPLATE_BODY_SUFFIX;

        // inject common data to dataMap
        injectGlobalEmailData(dataMap, sdSiteConfigBean);
        dataMap.put(EMAIL_BASIC_TEMPLATE_ID, templateId);
        dataMap.put(EMAIL_BASIC_RECIPIENT, recipient);

        // build html content
        String subjectString = btuTemplateEngineService.buildHtmlStringFromHtmlFile(subjectTemplate, dataMap);
        String htmlBodyString = btuTemplateEngineService.buildHtmlStringFromHtmlFile(bodyTemplate, dataMap);

        // add subject prefix to non-production email
        if( ! sdSiteConfigService.isProductionServer() ){
            subjectString = String.format("[%s] %s", sdSiteConfigBean.getServerType(), subjectString);
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
        if(sdSiteConfigService.isDevelopmentServer()) {
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
        String templateId = SdEmailBean.ERROR_STACK_TRACE_EMAIL.TEMPLATE_ID;

        // escape stacktrace
        String errorStackTraceString = ExceptionUtils.getStackTrace(e);
        errorStackTraceString = errorStackTraceString.replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
        errorStackTraceString = errorStackTraceString.replaceAll("(\t)", "&nbsp;&nbsp;&nbsp;&nbsp;");

        // prepare email data
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(SdEmailBean.ERROR_STACK_TRACE_EMAIL.EMAIL_SUBJECT, subjectText);
        dataMap.put(SdEmailBean.ERROR_STACK_TRACE_EMAIL.EMAIL_BODY, errorStackTraceString);

        send(templateId, recipient, dataMap);
    }

    private void injectGlobalEmailData(Map<String, Object> dataMap, SdSiteConfigBean sdSiteConfigBean) {
        if(dataMap==null){
            dataMap = new HashMap<>();
        }

        String webUrl = sdSiteConfigBean.getAppHttpsUrl();
        String webName = sdSiteConfigBean.getAppName();

        dataMap.put(EMAIL_GLOBAL_CONTACT_WEB_URL, webUrl);
        dataMap.put(EMAIL_GLOBAL_CONTACT_WEB_NAME, webName);
        dataMap.put(EMAIL_GLOBAL_LOGO_CID, EMAIL_GLOBAL_LOGO_CID);
        dataMap.put(EMAIL_GLOBAL_LOGO_HKT_CID, EMAIL_GLOBAL_LOGO_HKT_CID);
        dataMap.put(EMAIL_GLOBAL_LOGO_PCCW_GRP_CID, EMAIL_GLOBAL_LOGO_PCCW_GRP_CID);
    }

    private InternetAddress buildSenderProfile(SdSiteConfigBean sdSiteConfigBean){
        try {
            String fromAddress = sdSiteConfigBean.getMailUsername();
            String fromName = sdSiteConfigBean.getAppName();
            return new InternetAddress(fromAddress, fromName);
        } catch (UnsupportedEncodingException | NullPointerException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }
}
