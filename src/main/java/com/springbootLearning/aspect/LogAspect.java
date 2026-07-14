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
import com.springbootLearning.annotation.LogRequired;

import java.util.Arrays;



@Aspect
@Component
public class LogAspect {
    
    /**
     * 1. 定义切入点 (Pointcut)
     * execution 表达式含义：拦截 com.springbootLearning.controller.MainController 类下的所有方法，不论参数和返回值是什么
    // TODO: 这种写法是“一刀切”，不够灵活。因为这种写法是“一刀切”，不够灵活。
        目前最流行的做法是：自定义一个注解（比如 @LogRequired），只要研发人员在哪个方法上打了这个注解，哪个方法就被切面拦截。   
     */
    @Pointcut("execution(* com.springbootLearning.controller.MainController.*(..))")
    public void logPointCut() {}

    /**
     * 2. 前置通知 (Before)：在目标方法执行之前运行
     */
    /* 
    // TODO: 一刀切写法；不推荐使用。
    @Before("@annotation(logRequired)")
    public void doBefore(JoinPoint joinPoint, LogRequired logRequired) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        System.out.println("⏩ 【前置通知】方法 [" + methodName + "] 准备执行，参数为: " + Arrays.toString(args));
    } */
    /**
     *  TODO: 注解写法；推荐使用。
     *  <h1>四、 💡 AOP 底层避坑红线：什么是“方法内自调用失效”？</h1>
        这是 AOP 最经典、无数高级程序员都踩过踩痛的死穴。
        假设你在同一个类 OrderService 里写了两个方法：
        ```Java
        @Service
        public class OrderService {
            public void noAsync() {
                System.out.println("方法 A 被调用...");
                this.makeOrder(); // ❌ 致命隐患：直接用 this 关键字在内部调用了带注解的方法 B
            }

            @LogRequired("核心下单")
            public void makeOrder() {
                System.out.println("执行下单...");
            }
        }
        ```
        💥 结果：如果你调用 noAsync()，你会惊恐地发现 @LogRequired 的切面代码居然完全失效了！
        为什么会失效？
        我们在讲生命周期时提到过，AOP 的本质是 Spring 拦截原生的 Bean，在内存中生成了一个“代理对象（Proxy）”。
        当你从外部调用 orderService.makeOrder() 时，由于 Controller 拿到的是代理对象，所以它会先走代理代码（触发切面），再走原生代码。

        但是！如果你调用的是 noAsync()，代理对象确实被触发了；但在 noAsync() 内部，Java 底层是通过 this.makeOrder() 去执行的。this 代表的是当前这个原生的、肉身类对象，而不是代理对象！ 绕过了代理对象，AOP 自然就彻底瞎眼失效了。

        🛠️ 怎么解决自调用失效？
        最标准做法（解耦）：不要在同一个类里自调用，把方法 B 拆到另一个 Service 里去。

        利用 AopContext（不推荐，略显臃肿）：强行从 Spring 上下文中把当前类的代理对象捞出来调用：
        ((OrderService) AopContext.currentProxy()).makeOrder();

        延迟依赖注入自己（大厂常用黑科技）：

        ```Java
        @Service
        public class OrderService {
            @Autowired
            @Lazy // 延迟加载，防止循环依赖死锁
            private OrderService self; // 注入自己的代理类

            public void noAsync() {
                self.makeOrder(); // ✅ 走代理调用，切面成功触发！
            }
        }
     * ```
     */
    @Before("@annotation(logRequired)") // 拦截所有被 @LogRequired 注解标记的方法，这里 logRequired 是注解实例，必须这么写
    public void doBefore(JoinPoint joinPoint, LogRequired logRequired) {
        System.out.println("🚩 【注解切面触发】当前执行的操作是: " + logRequired.description());
        System.out.println("🚩 执行的方法是: " + joinPoint.getSignature().getName() + "，入参是：" + Arrays.toString(joinPoint.getArgs()));
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
