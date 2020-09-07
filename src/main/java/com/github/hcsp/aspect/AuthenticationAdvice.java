package com.github.hcsp.aspect;

import com.github.hcsp.entity.BlogResult;
import com.github.hcsp.entity.User;
import com.github.hcsp.service.impl.AuthService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;


@Aspect
@Component
public class AuthenticationAdvice {

    @Resource
    private AuthService authService;

    public static int userId;

    private AuthenticationAdvice() {

    }

    @Around("@annotation(com.github.hcsp.aspect.AuthenticationAspect)")
    public Object cacheAdvice(ProceedingJoinPoint process) {
        // 获取用户ID
        Optional<User> currentUser = authService.getCurrentUser();
        if (!currentUser.isPresent()) {
            return BlogResult.failure("登录后才能操作");
        }
        userId = currentUser.get().getId();
        Object proceed = null;
        try {
            proceed = process.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return proceed;
    }
}
