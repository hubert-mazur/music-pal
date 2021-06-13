package com.hm.zti.fis.musicpal.aspect;

import lombok.extern.java.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Log
@Component
@Aspect
public class Logger {
    @After("execution(public * com.hm.zti.fis.musicpal.login..*.*(..)) || execution(public * com.hm.zti.fis.musicpal.registration..*.*(..)) || execution(public * com.hm.zti.fis.musicpal.person..*.*(..)) || execution(public * com.hm.zti.fis.musicpal.event..*.*(..)) || execution(public * com.hm.zti.fis.musicpal.exceptions..*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("[LOGGER]: " + joinPoint.getTarget().getClass().getSimpleName()
                + " run public method " + joinPoint.getSignature().getName()
        );
    }
}
