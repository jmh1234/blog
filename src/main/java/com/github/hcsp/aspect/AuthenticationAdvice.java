package com.github.hcsp.aspect;

import com.alibaba.fastjson.JSONObject;
import com.github.hcsp.entity.LoginResult;
import com.github.hcsp.utils.Regex;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class AuthenticationAdvice {

    private AuthenticationAdvice() {

    }

    @Around("@annotation(com.github.hcsp.aspect.AuthenticationAspect)")
    public Object cacheAdvice(ProceedingJoinPoint process) {
        Object[] args = process.getArgs();
        LoginResult regex = Regex.isMatch((JSONObject) args[0]);
        if (regex.getIsLogin()) {
            Object proceed = null;
            try {
                proceed = process.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return proceed;
        } else {
            return regex;
        }
    }
}
