package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.service.BtuEmailService;
import com.hkt.btu.common.core.service.BtuSiteConfigService;
import com.hkt.btu.common.core.service.BtuTemplateEngineService;
import com.hkt.btu.common.core.service.bean.BtuEmailBean;
import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import org.apache.commons.lang3.StringUtils;
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
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static com.hkt.btu.common.core.service.bean.BtuEmailBean.*;

public class BtuEmailServiceImpl implements BtuEmailService {

    private static final Logger LOG = LogManager.getLogger(BtuEmailServiceImpl.class);

    protected static final String EMAIL_TEMPLATE_DIR = "email/";
    protected static final String EMAIL_TEMPLATE_SUBJECT_SUFFIX = "_subject";
    protected static final String EMAIL_TEMPLATE_BODY_SUFFIX = "_body";

    @Resource(name = "siteConfigService")
    BtuSiteConfigService siteConfigService;
    @Resource(name = "templateEngineService")
    BtuTemplateEngineService btuTemplateEngineService;

    protected InternetAddress senderProfile;
    protected JavaMailSender javaMailSender;

    protected JavaMailSender getJavaMailSender() {
        if (javaMailSender == null) {
            reload();
        }
        return javaMailSender;
    }

    @Override
    public void reload() {
        BtuSiteConfigBean siteConfigBean = siteConfigService.getSiteConfigBean();

        JavaMailSenderImpl newJavaMailSender = new JavaMailSenderImpl();
        newJavaMailSender.setHost(siteConfigBean.getMailHost());
        newJavaMailSender.setPort(siteConfigBean.getMailPort());
        newJavaMailSender.setUsername(siteConfigBean.getMailUsername());

        Properties props = newJavaMailSender.getJavaMailProperties();
        props.put("mail.debug", "true");
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.from", siteConfigBean.getMailUsername());
        props.put("mail.transport.protocol", "smtp");

        // set send profile
        senderProfile = buildSenderProfile(siteConfigBean);

        // swap in the new mail config
        javaMailSender = newJavaMailSender;

        LOG.info("BtuEmailService reloaded.");

    }

    @Override
    public void send(String recipient, String subjectText, String bodyText) throws MessagingException {
        send(new String[]{recipient}, subjectText, bodyText);
    }
    @Override
    public void send(String recipient, String recipientName, String subjectText, String bodyText) throws MessagingException {
        String templateId = BtuEmailBean.DEFAULT_EMAIL.TEMPLATE_ID;

        // prepare email data
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(EMAIL_BASIC_RECIPIENT, recipientName);
        dataMap.put(BtuEmailBean.DEFAULT_EMAIL.EMAIL_SUBJECT, subjectText);
        dataMap.put(BtuEmailBean.DEFAULT_EMAIL.EMAIL_BODY, bodyText);

        send(templateId, recipient, null, dataMap);
    }


    @Override
    public void send(String templateId, String recipient, File file, Map<String, Object> dataMap) throws MessagingException {
        dataMap = dataMap==null ? new HashMap<>() : dataMap;
        dataMap.put(EMAIL_BASIC_RECIPIENT, recipient);
        send(templateId, new String[]{recipient}, file, dataMap);
    }

    @Override
    public void sendErrorStackTrace(String recipient, String subjectText, Exception e) throws MessagingException {
        sendErrorStackTrace(new String[]{recipient}, subjectText, e);
    }

    @Override
    public void injectGlobalEmailData(Map<String, Object> dataMap, BtuSiteConfigBean siteConfigBean) {
        if (dataMap != null) {
            String webUrl = siteConfigBean.getAppHttpsUrl();
            String webName = siteConfigBean.getAppName();

            dataMap.put(EMAIL_GLOBAL_CONTACT_WEB_URL, webUrl);
            dataMap.put(EMAIL_GLOBAL_CONTACT_WEB_NAME, webName);
            dataMap.put(EMAIL_GLOBAL_LOGO_CID, EMAIL_GLOBAL_LOGO_CID);
            dataMap.put(EMAIL_GLOBAL_LOGO_HKT_CID, EMAIL_GLOBAL_LOGO_HKT_CID);
            dataMap.put(EMAIL_GLOBAL_LOGO_PCCW_GRP_CID, EMAIL_GLOBAL_LOGO_PCCW_GRP_CID);
        }
    }

    @Override
    public void injectGlobalEmailInlineAttachment(MimeMessageHelper messageHelper) throws MessagingException {
        if (messageHelper != null) {
            messageHelper.addInline(EMAIL_GLOBAL_LOGO_CID, EMAIL_GLOBAL_LOGO_RESOURCE, CONTENT_TYPE_PNG);
            messageHelper.addInline(EMAIL_GLOBAL_LOGO_HKT_CID, EMAIL_GLOBAL_LOGO_HKT_RESOURCE, CONTENT_TYPE_PNG);
            messageHelper.addInline(EMAIL_GLOBAL_LOGO_PCCW_GRP_CID, EMAIL_GLOBAL_LOGO_PCCW_GRP_RESOURCE, CONTENT_TYPE_PNG);
        }
    }

    @Override
    public void send(String[] recipients, String subjectText, String bodyText) throws MessagingException {
        String templateId = BtuEmailBean.DEFAULT_EMAIL.TEMPLATE_ID;

        // prepare email data
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(BtuEmailBean.DEFAULT_EMAIL.EMAIL_SUBJECT, subjectText);
        dataMap.put(BtuEmailBean.DEFAULT_EMAIL.EMAIL_BODY, bodyText);

        send(templateId, recipients, null, dataMap);
    }

    @Override
    public void send(String templateId, String[] recipients, File file, Map<String, Object> dataMap) throws MessagingException {
        send (templateId, recipients, null, file, dataMap);

    }
    @Override
    public void send(String templateId, String[] recipients, String[] ccRecipients, File file, Map<String, Object> dataMap) throws MessagingException {
        BtuSiteConfigBean sdSiteConfigBean = siteConfigService.getSiteConfigBean();

        String subjectTemplate = EMAIL_TEMPLATE_DIR + templateId + EMAIL_TEMPLATE_SUBJECT_SUFFIX;
        String bodyTemplate = EMAIL_TEMPLATE_DIR + templateId + EMAIL_TEMPLATE_BODY_SUFFIX;

        // inject common data to dataMap
        dataMap = dataMap==null ? new HashMap<>() : dataMap;

        dataMap.put(EMAIL_BASIC_TEMPLATE_ID, templateId);
        injectGlobalEmailData(dataMap, sdSiteConfigBean);

        // build html content
        String subjectString = btuTemplateEngineService.buildHtmlStringFromHtmlFile(subjectTemplate, dataMap);
        String htmlBodyString = btuTemplateEngineService.buildHtmlStringFromHtmlFile(bodyTemplate, dataMap);

        // add subject prefix to non-production email
        if (!siteConfigService.isProductionServer()) {
            subjectString = String.format("[%s] %s", sdSiteConfigBean.getServerType(), subjectString);
        }

        send(recipients, ccRecipients, subjectString, htmlBodyString, file);

    }

    @Override
    public void sendErrorStackTrace(String[] recipients, String subjectText, Exception e) throws MessagingException {
        String templateId = BtuEmailBean.ERROR_STACK_TRACE_EMAIL.TEMPLATE_ID;

        // escape stacktrace
        String errorStackTraceString = ExceptionUtils.getStackTrace(e);
        errorStackTraceString = errorStackTraceString.replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
        errorStackTraceString = errorStackTraceString.replaceAll("(\t)", "&nbsp;&nbsp;&nbsp;&nbsp;");

        // prepare email data
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(BtuEmailBean.ERROR_STACK_TRACE_EMAIL.EMAIL_SUBJECT, subjectText);
        dataMap.put(BtuEmailBean.ERROR_STACK_TRACE_EMAIL.EMAIL_BODY, errorStackTraceString);

        send(templateId, recipients, null, dataMap);
    }

    @Override
    public void send(String[] recipients, String[] ccRecipients, String subject, String bodyText) throws MessagingException {
        send(recipients, ccRecipients, subject, bodyText, null);
    }

    @Override
    public void send(String[] recipients, String[] ccRecipients, String subject, String bodyText, File file) throws MessagingException {
        JavaMailSender mailSender = getJavaMailSender();
        // build email object
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true,"utf-8");
        // empty cc or recipients may throw error (from PPMS)
        boolean emptyTo = false;
        boolean emptyCc = false;
        if (StringUtils.isAllEmpty(recipients) && StringUtils.isAllEmpty(ccRecipients))
            throw new IllegalArgumentException("Empty Recipient!");
        if (!StringUtils.isAllEmpty(recipients)) {
            if (!StringUtils.isAnyEmpty(recipients))
                messageHelper.setTo(recipients);
            else {
                ArrayList<String> removeEmpty = new ArrayList<String>();
                for (String to : recipients) {
                    if (StringUtils.isAllEmpty(to))
                        continue;
                    removeEmpty.add(to);
                }
                if (removeEmpty.size() == 0) {
                    emptyTo = true;
                } else {
                    messageHelper.setTo(removeEmpty.toArray(new String[removeEmpty.size()]));
                }
            }
        }
        if (!StringUtils.isAllEmpty(ccRecipients)) {
            if (!StringUtils.isAnyEmpty(ccRecipients))
                messageHelper.setCc(ccRecipients);
            else {
                ArrayList<String> removeEmpty = new ArrayList<String>();
                for (String cc : ccRecipients) {
                    if (StringUtils.isAllEmpty(cc))
                        continue;
                    removeEmpty.add(cc);
                }
                if (removeEmpty.size() == 0) {
                    emptyCc = true;
                } else {
                    messageHelper.setCc(removeEmpty.toArray(new String[removeEmpty.size()]));
                }
            }
        }
        if (emptyTo && emptyCc) {
            throw new IllegalArgumentException("Empty Recipient!");
        }
        messageHelper.setSubject(subject);
        messageHelper.setText(bodyText, true);
        if (file != null) {
            messageHelper.addAttachment(file.getName(), file);
        }
        if (senderProfile != null) {
            messageHelper.setFrom(senderProfile);
        }

        // add inline img
//        messageHelper.addInline(EMAIL_GLOBAL_LOGO_CID, EMAIL_GLOBAL_LOGO_RESOURCE, CONTENT_TYPE_PNG);
//        messageHelper.addInline(EMAIL_GLOBAL_LOGO_HKT_CID, EMAIL_GLOBAL_LOGO_HKT_RESOURCE, CONTENT_TYPE_PNG);
        injectGlobalEmailInlineAttachment(messageHelper);
        //messageHelper.addInline(EMAIL_GLOBAL_LOGO_HKT_AND_PCCW_GRP_CID, EMAIL_GLOBAL_LOGO_HKT_AND_PCCW_GRP_RESOURCE, CONTENT_TYPE_PNG);
        //messageHelper.addInline(EMAIL_GLOBAL_LOGO_HKT_CID, EMAIL_GLOBAL_LOGO_HKT_RESOURCE, CONTENT_TYPE_PNG);


        // send email
        if (siteConfigService.isDevelopmentServer()) {
            LOG.warn("Email Recipient: " + Arrays.toString(recipients));
            LOG.warn("Email CC Recipient: " + Arrays.toString(ccRecipients));
            LOG.warn("Email Subject: " + subject);
            LOG.warn("Email Body: \n" + bodyText);
            LOG.warn("CANNOT send above email in development environment.");
        } else {
            mailSender.send(message);
            LOG.info("Sent email [" + subject + "] to " + Arrays.toString(recipients) + ".");
        }
    }


    protected InternetAddress buildSenderProfile(BtuSiteConfigBean sdSiteConfigBean) {
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
