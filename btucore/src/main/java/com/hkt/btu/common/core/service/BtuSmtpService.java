package com.hkt.btu.common.core.service;

import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;

import java.io.File;
import java.util.Map;

public interface BtuSmtpService {

    void send(String templateId, String recipient, File file,Map<String, Object> dataMap);

    void injectGlobalEmailData(Map<String, Object> dataMap, BtuSiteConfigBean siteConfigBean);

    // add send mail to multiple recipients
    void send(String templateId, String[] recipients, File file,Map<String, Object> dataMap);

    void send(String templateId, String[] recipients, String[] ccRecipients, File file, Map<String, Object> dataMap);

    void send(String[] recipients, String[] ccRecipients, String subject, String bodyText, File file);
}
