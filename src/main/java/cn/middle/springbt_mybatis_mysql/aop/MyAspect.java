package cn.middle.springbt_mybatis_mysql.aop;

import cn.middle.springbt_mybatis_mysql.entity.User;
import com.google.gson.Gson;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author 15510
 * @create 2019-06-20 16:44
 */

@Aspect
@Component
public class MyAspect {
    private static Logger logger = LoggerFactory.getLogger(MyAspect.class);

    @Pointcut("execution(* cn.middle.springbt_mybatis_mysql.controller.*.*(..))")//controller包中的所有类所有方法(包含的参数任意)
    public void weblog() {

    }

    /**
     * 前置通知：方法调用前被调用
     */
    @Before("weblog()")
    public void before(JoinPoint joinPoint){
        //JoinPoint对象封装了SpringAop中切面方法的信息,在切面方法中添加JoinPoint参数,就可以获取到封装了该方法信息的JoinPoint对象.

        logger.info("前置通知");

        /*
        //获取传入目标方法的参数对象，目标方法有参数这个就有，目标方法没有参数这个就没有
        Object[] obj = joinPoint.getArgs();

        //获取代理对象（代理对象自己）
        Object aThis = joinPoint.getThis();
        System.out.println("joinPoint.getThis() = "+ aThis);
        //获取被代理的对象
        Object target = joinPoint.getTarget();
        System.out.println("joinPoint.getTarget() = "+ target);
        //代理对象自己和被代理对象输出的内存地址一样啊
        */

        //用的最多 通知的签名,获取封装了署名信息的对象,在该对象中可以获取到目标方法名,所属类的Class等信息
        Signature signature = joinPoint.getSignature();
        //代理的是哪一个方法
        logger.info("代理的是哪一个方法"+signature.getName());
        //AOP代理类的名字
        logger.info("AOP代理类的名字"+signature.getDeclaringTypeName());
        //AOP代理类的类（class）信息
        //signature.getDeclaringType();



        //下面是获取request请求信息，Session信息。
        //获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //从获取RequestAttributes中获取HttpServletRequest的信息
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        //如果要获取Session信息的话，可以这样写：
        //HttpSession session = (HttpSession) requestAttributes.resolveReference(RequestAttributes.REFERENCE_SESSION);
        //获取请求参数
        Enumeration<String> enumeration = request.getParameterNames();
        Map<String,String> parameterMap = new HashMap<>();
        while (enumeration.hasMoreElements()){
            String parameter = enumeration.nextElement();
            parameterMap.put(parameter,request.getParameter(parameter));
        }
        Gson gson = new Gson();
        String s = gson.toJson(parameterMap);
        System.out.println("传入的parameter" + s);

    }



    /**
     * 后置返回通知
     * 这里需要注意的是:
     *      如果参数中的第一个参数为JoinPoint，则第二个参数为返回值的信息
     *      如果参数中的第一个参数不为JoinPoint，则第一个参数为returning中对应的参数
     * returning：用于接收切点的返回值
     *       限定了只有目标方法返回值与通知方法相应参数类型时才能执行后置返回通知，否则不执行，
     *       对于returning对应的通知方法参数为Object类型将匹配任何目标返回值
     * @param joinPoint
     * @param res
     */
    @AfterReturning(pointcut = "weblog()",returning = "res")
    public void doAfterReturningAdvice1(JoinPoint joinPoint,Object res){
        logger.info("@AfterReturning第一个后置返回通知的返回值："+res);
    }

    @AfterReturning(value = "weblog()",returning = "res",argNames = "res")//argNames不知道有啥用，去掉也没关系
    public void doAfterReturningAdvice2(List<User> res){
        logger.info("@AfterReturning第二个后置返回通知的返回值："+res);
    }


    /**
     * 后置异常通知
     *  定义一个名字，该名字用于匹配通知实现方法的一个参数名，当目标方法抛出异常返回后，将把目标方法抛出的异常传给通知方法；
     *  throwing:限定了只有目标方法抛出的异常与通知方法相应参数异常类型时才能执行后置异常通知，否则不执行，
     *            对于throwing对应的通知方法参数为Throwable类型将匹配任何异常。
     * @param joinPoint
     * @param exception
     */
    @AfterThrowing(value = "weblog()",throwing = "exception")
    public void doAfterThrowingAdvice(JoinPoint joinPoint,Throwable exception){
        logger.info(joinPoint.getSignature().getName());
        if(exception instanceof NullPointerException){
            logger.info("@AfterThrowing发生了空指针异常!!!!!");
        }
    }

    @After(value = "weblog()")
    public void doAfterAdvice(JoinPoint joinPoint){
        logger.info("@After后置通知执行了!");
    }



    /**
     * 环绕通知：
     *   注意:Spring AOP的环绕通知会影响到AfterThrowing通知的运行,不要同时使用
     *
     *   环绕通知非常强大，可以决定目标方法是否执行，什么时候执行，执行时是否需要替换方法参数，执行完毕是否需要替换返回值。
     *   环绕通知第一个参数必须是org.aspectj.lang.ProceedingJoinPoint类型
     */
    @Around(value = "weblog()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint){
        logger.info("@Around环绕通知："+proceedingJoinPoint.getSignature().toString());
        Object obj = null;
        try {
            obj = proceedingJoinPoint.proceed(); //可以加参数
            logger.info(obj.toString());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        logger.info("@Around环绕通知执行结束");
        return obj;
    }

}
