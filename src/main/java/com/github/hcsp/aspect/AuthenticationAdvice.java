package com.github.hcsp.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthenticationAdvice {
    @Around("@annotation(com.github.hcsp.aspect.AuthenticationAspect)")
    public Object cacheAdvice(ProceedingJoinPoint process) {
        Object proceed = null;
        try {
            proceed = process.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return proceed;
    }
}
