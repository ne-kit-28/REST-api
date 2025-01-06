package com.example.demo.audit;

import com.example.demo.service.KafkaSenderService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
//@Order(1)
public class AspectAudit {

    private final KafkaSenderService kafkaSenderService;

    public AspectAudit(KafkaSenderService kafkaSenderService) {
        this.kafkaSenderService = kafkaSenderService;
    }

    @Pointcut("@annotation(AuditAction)")
    public void pointcut() {}

    //@Before("pointcut()")
    @Before("@annotation(auditAction)")
    public void logAudit(JoinPoint joinPoint, AuditAction auditAction) {
        if (auditAction.action().isEmpty()) {
            //logger.info("Пользователь совершил действие: {}", auditAction);

            System.out.println(joinPoint.getSignature().getName() + " Выполнен");
            kafkaSenderService.sendAudit(joinPoint.getSignature().getName() + " Выполнен");
        } else {
            System.out.println(auditAction.action() + " Выполнен ");
            kafkaSenderService.sendAudit(auditAction.action() + " Выполнен ");
        }
    }
}
