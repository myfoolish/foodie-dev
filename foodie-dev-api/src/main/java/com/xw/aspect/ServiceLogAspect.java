package com.xw.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author liuxiaowei
 * @Description 通过日志记录每个 service 执行的时间
 *          AOP 通知：
 *              1、前置通知：在方法调用之前执行
 *              2、后置通知：在方法正常调用之后执行（不能有异常，否则此通知不能执行）
 *              3、环绕通知：在方法调用之前和之后，都分别可以执行通知
 *              4、异常通知：如果在方法调用过程中发生异常，则通知
 *              5、最终通知：在方法调用之后执行（如有异常，此通知是可以执行的），可理解为try catch 中的 finally
 * @date 2021/12/30
 */
@Aspect
@Component
public class ServiceLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    /**
     * 切面表达式：
     *  execution 代表所要执行的表达式主体
     *      第一处 * 代表方法返回类型，* 代表所有类型
     *      第二处 包名代表 aop 所监控的类所在的包
     *      第三处 .. 代表该包以及其子包下的所有类方法
     *      第四处 * 代表类名，* 代表所有类
     *      第五处 *(..) * 代表类中的方法名，(..) 表示方法中的任何参数
     * @param joinPoint
     * @return
     */
    @Around("execution(* com.xw.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) {

        logger.info("====== 开始执行 {}.{} ======",
                joinPoint.getTarget().getClass(), joinPoint.getSignature().getName());

        // 记录开始时间
        long begin = System.currentTimeMillis();
        // 执行目标 service
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        // 记录结束时间
        long end = System.currentTimeMillis();
        long takeTime = end - begin;
        if (takeTime > 3000) {
            logger.error("====== 执行结束，耗时：{} 毫秒 ======", takeTime);
        } else if (takeTime > 2000) {
            logger.warn("====== 执行结束，耗时：{} 毫秒 ======", takeTime);
        } else {
            logger.info("====== 执行结束，耗时：{} 毫秒 ======", takeTime);
        }
        return result;
    }
}
