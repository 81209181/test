package com.hkt.btu.common.core.service;


import org.thymeleaf.exceptions.TemplateInputException;

import java.util.Map;

public interface BtuTemplateEngineService {
    String buildHtmlStringFromHtmlFile(String template, Map<String, Object> map) throws TemplateInputException;
    String buildHtmlStringFromString(String htmlTemplateString, Map<String, Object> map);
}
