package com.hkt.btu.common.core.service;

import javax.mail.MessagingException;
import java.util.Map;

public interface BtuEmailService {
    void reload();

    void send(String recipient, String subjectText, String bodyText) throws MessagingException;

    void send(String templateId, String recipient, Map<String, Object> dataMap) throws MessagingException;

    void sendErrorStackTrace(String recipient, String subjectText, Exception e) throws MessagingException;
}
