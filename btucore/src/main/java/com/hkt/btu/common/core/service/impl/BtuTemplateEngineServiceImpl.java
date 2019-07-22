package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.service.BtuTemplateEngineService;
import org.apache.commons.collections4.MapUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import javax.annotation.Resource;
import java.util.Map;

public class BtuTemplateEngineServiceImpl implements BtuTemplateEngineService {

    @Resource
    private TemplateEngine templateEngine;

    private SpringTemplateEngine stringTemplateEngine;

    @Override
    public String buildHtmlStringFromHtmlFile(String template, Map<String, Object> map)  throws TemplateInputException {
        Context context = buildContext(map);
        return templateEngine.process(template, context);
    }

    @Override
    public String buildHtmlStringFromString(String htmlTemplateString, Map<String, Object> map) {
        Context context = buildContext(map);
        return getStringTemplateEngine().process(htmlTemplateString, context);
    }

    private Context buildContext(Map<String, Object> map){
        Context context = new Context();
        if(MapUtils.isNotEmpty(map)) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }
        }
        return context;
    }

    private SpringTemplateEngine getStringTemplateEngine() {
        if(stringTemplateEngine==null){
            StringTemplateResolver resolver = new StringTemplateResolver();
            resolver.setTemplateMode(TemplateMode.HTML);

            SpringStandardDialect dialect = new SpringStandardDialect();
            dialect.setEnableSpringELCompiler(true);

            SpringTemplateEngine newStringTemplateEngine = new SpringTemplateEngine();
            newStringTemplateEngine.setDialect(dialect);
            newStringTemplateEngine.setEnableSpringELCompiler(true);
            newStringTemplateEngine.setTemplateResolver(resolver);

            stringTemplateEngine = newStringTemplateEngine;
        }
        return stringTemplateEngine;
    }
}
