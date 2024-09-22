package com.revshop.utility;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Log4j {

    // Create a logger instance for logging
    private final Logger logger = LoggerFactory.getLogger("RevShopLogger");

    // Pointcut that matches all methods in the com.revshop package and its sub-packages
    @Pointcut("execution(* com.revshop..*(..))")
    public void logAllMethods() {}

    // Log the method entry (Before advice)
    @Before("logAllMethods()")
    public void logMethodEntry(JoinPoint joinPoint) {
        logger.info("Entering method: " + joinPoint.getSignature().getName() + " in class: " + joinPoint.getSignature().getDeclaringTypeName());
    }

    // Log the method exit (AfterReturning advice)
    @AfterReturning(pointcut = "logAllMethods()", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        logger.info("Exiting method: " + joinPoint.getSignature().getName() + " in class: " + joinPoint.getSignature().getDeclaringTypeName() + " with result: " + result);
    }
}
