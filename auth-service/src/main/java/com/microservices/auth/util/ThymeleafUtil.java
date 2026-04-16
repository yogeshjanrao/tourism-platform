package com.microservices.auth.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class ThymeleafUtil {

    @Autowired
    private TemplateEngine templateEngine;

    public String process(String templateName, Context context) {
        return templateEngine.process(templateName, context);
    }
}
