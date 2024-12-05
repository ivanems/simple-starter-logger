package ru.ivanems.logger.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import ru.ivanems.logger.config.LoggerProps;

@Aspect
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    private final Level levelLog;

    public LogAspect(LoggerProps loggerProps) {
        levelLog = Level.valueOf(loggerProps.getLevel());
    }

    private void log (String message, Object... placeholders) {
        logger.atLevel(levelLog).log(message, placeholders);
    }

    @Before(
            "@annotation(ru.ivanems.logger.aspect.util.LogBeforeExecution)"
    )
    public void logBeforeExecution(JoinPoint joinPoint) {
        log("Method {} with arg {} is about to be executed", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @AfterThrowing(
            pointcut = "@annotation(ru.ivanems.logger.aspect.util.LogAfterThrowing)",
            throwing = "exception"
    )
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        log("Exception was thrown in method: {}, message: {}", joinPoint.getSignature(), exception.getMessage());
    }

    @AfterReturning(
            pointcut = "@annotation(ru.ivanems.logger.aspect.util.LogAfterReturning)",
            returning = "result"
    )
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log("Method {} returned: {}", joinPoint.getSignature(), result);
    }

    @Around(
            "@annotation(ru.ivanems.logger.aspect.util.LogTimeExecution)"
    )
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) {
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            log("Exception was thrown while executing method: {}, with message: {}", joinPoint.getSignature(), e.getMessage());
            throw new RuntimeException(e);
        } finally {
            long executionTime = System.currentTimeMillis() - start;
            log("Method {} execution time: {} ms", joinPoint.getSignature(), executionTime);
        }
    }


}
