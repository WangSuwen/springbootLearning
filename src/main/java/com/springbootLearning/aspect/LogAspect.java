package com.springbootLearning.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;



import java.util.Arrays;



@Aspect
@Component
public class LogAspect {
    
    /**
     * 1. 定义切入点 (Pointcut)
     * execution 表达式含义：拦截 com.springbootLearning.controller.MainController 类下的所有方法，不论参数和返回值是什么
     */
    @Pointcut("execution(* com.springbootLearning.controller.MainController.*(..))")
    public void logPointCut() {}

    /**
     * 2. 前置通知 (Before)：在目标方法执行之前运行
     */
    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        System.out.println("⏩ 【前置通知】方法 [" + methodName + "] 准备执行，参数为: " + Arrays.toString(args));
    }
    
    /**
     * 3. 后置通知 (After)：在目标方法执行之后运行
     */
    @After("logPointCut()")
    public void doAfter(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("⏩ 【后置通知】方法 [" + methodName + "] 执行完成");
    }

    /**
     * 4. 返回通知 (AfterReturning)：在目标方法【正常成功返回】之后执行，可以拿到返回值
     */
    @AfterReturning(value = "logPointCut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("⏩ 【返回通知】方法 [" + methodName + "] 执行完成，返回值为: " + result);
    }

    /**
     * 5. 异常通知 (AfterThrowing)：在目标方法【抛出异常】之后执行，可以拿到异常信息
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("⏩ 【异常通知】方法 [" + methodName + "] 抛出异常，异常信息为: " + e.getMessage());
    }

    /**
     * 6. 环绕通知 (Around)：在目标方法【正常执行】或【抛出异常】之后执行，不能拿到返回值或异常信息
     */
    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        System.out.println("🛸 【环绕通知】==== 环绕开启 ====");

        // 核心特权：拿到了参数，我们甚至可以在这里判断如果角色不匹配，直接拦截不调用 proceed()
        /* Object[] args = joinPoint.getArgs();
        String role = (String) args[1];
        if (!"SUPER_ADMIN".equals(role)) {
            System.out.println("🛸 【环绕通知】检测到非超级管理员，直接劝退拦截！");
            return "FAILED_NO_PERMISSION"; // 偷梁换柱：目标方法压根不会执行，直接给前端返回失败标记
        } */

        try {
            // 🌟 这一行代码代表放行：去执行真正的目标业务方法
            Object result = joinPoint.proceed(); 
            
            long endTime = System.currentTimeMillis();
            System.out.println("🛸 【环绕通知】核心方法耗时: " + (endTime - startTime) + "ms");
            return result;
        } catch (Throwable e) {
            System.out.println("🛸 【环绕通知】在环绕里发现了异常，继续向上抛出...");
            throw e;
        } finally {
            System.out.println("🛸 【环绕通知】==== 环绕关闭 ====");
        }
    }
}
