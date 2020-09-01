package com.github.hcsp.aspect;

import com.github.hcsp.entity.CacheKey;
import com.github.hcsp.entity.CacheValue;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class CacheAdvice {

    private static final Object object = new Object();

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private CacheAdvice() {

    }

    @Around("@annotation(com.github.hcsp.aspect.CacheAspect)")
    public Object cacheAdvice(ProceedingJoinPoint process) {
        MethodSignature methodSignature = (MethodSignature) process.getSignature();
        Method method = methodSignature.getMethod();
        boolean cacheIsValid = false;

        // 判断缓存是否失效
        CacheKey cacheKey = new CacheKey(method.getName(), object, process.getArgs());
        CacheValue cacheValue = (CacheValue) redisTemplate.opsForValue().get(String.valueOf(cacheKey.hashCode()));
        int i = method.getAnnotation(CacheAspect.class).cacheSecond();
        if (cacheValue != null && System.currentTimeMillis() - cacheValue.getTime() <= i * 1000) {
            cacheIsValid = true;
        }

        Object proceed = null;
        try {
            // 从缓存中返回视图
            if (cacheIsValid) {
                Map<String, Object> model = new HashMap<>();
                model.put("items", cacheValue.getValue());
                proceed = new ModelAndView("index", model);
            } else {
                proceed = process.proceed();
                ModelAndView model = (ModelAndView) proceed;
                CacheValue insertValue = new CacheValue(model.getModel().get("items"), System.currentTimeMillis());
                redisTemplate.opsForValue().set(String.valueOf(cacheKey.hashCode()), insertValue);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return proceed;
    }
}
