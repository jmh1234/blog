package com.github.hcsp.aspect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CacheAspect {
    int cacheSecond() default 60;
}
