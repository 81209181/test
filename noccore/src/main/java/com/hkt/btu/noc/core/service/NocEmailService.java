package com.hkt.btu.noc.core.service;

import javax.mail.MessagingException;
import java.util.Map;

public interface NocEmailService {

    void reload();

    void send(String recipient, String subjectText, String bodyText) throws MessagingException;
    void send(String recipient, String templateId, Map<String, Object> dataMap) throws MessagingException;

    void sendErrorStackTrace(String recipient, String subjectText, Exception e) throws MessagingException;
}
