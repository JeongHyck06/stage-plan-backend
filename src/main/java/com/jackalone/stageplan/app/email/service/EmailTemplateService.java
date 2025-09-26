package com.jackalone.stageplan.app.email.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private final TemplateEngine templateEngine;

    public String createVerificationEmailTemplate(String verificationCode, String baseUrl) {
        Context context = new Context();
        context.setVariable("verificationCode", verificationCode);
        context.setVariable("baseUrl", baseUrl);
        
        return templateEngine.process("email/verification", context);
    }

    public String createVerificationEmailSubject() {
        return "StagePlan 이메일 인증 코드";
    }
}
