package com.hkt.btu.common.core.service;

import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import java.io.File;
import java.util.Map;

public interface BtuEmailService {
    void reload();

    void send(String recipient, String subjectText, String bodyText) throws MessagingException;
    void send(String recipient, String recipientName, String subjectText, String bodyText) throws MessagingException;

    void send(String templateId, String recipient, File file,Map<String, Object> dataMap) throws MessagingException;

    void sendErrorStackTrace(String recipient, String subjectText, Exception e) throws MessagingException;

    void injectGlobalEmailData(Map<String, Object> dataMap, BtuSiteConfigBean siteConfigBean);

    void injectGlobalEmailInlineAttachment(MimeMessageHelper messageHelper) throws MessagingException;

    // add send mail to multiple recipients
    void send(String[] recipients, String subjectText, String bodyText) throws MessagingException;

    void send(String templateId, String[] recipients, File file,Map<String, Object> dataMap) throws MessagingException;

    void send(String templateId, String[] recipients, String[] ccRecipients, File file, Map<String, Object> dataMap) throws MessagingException;

    void sendErrorStackTrace(String[] recipients, String subjectText, Exception e) throws MessagingException;

    void send(String[] recipients, String[] ccRecipients, String subject, String bodyText) throws MessagingException;

    void send(String[] recipients, String[] ccRecipients, String subject, String bodyText, File file) throws MessagingException;
}
