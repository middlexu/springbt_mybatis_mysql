# springboot-AOP

---

## 前言

今天我们来说说spring中的切面Aspect，这是Spring的一大优势。面向切面编程往往让我们的开发更加低耦合，也大大减少了代码量，同时呢让我们更专注于业务模块的开发，把那些与业务无关的东西提取出去，便于后期的维护和迭代。



---

## 引入依赖
> 修改`pom.xml`，添加`spring-boot-starter-aop`依赖

```xml
<!-- aop -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
    <version>2.1.4.RELEASE</version>
</dependency>
```



---

## AOP

### 定义切面类

首先定义一个切面类，加上 **@Aspect**  **@Component** 这两个注解 

```java
@Aspect
@Component
public class MyAspect {
    private static Logger logger = LoggerFactory.getLogger(MyAspect.class);//记录日志
    ...
       
```

### 定义切点

```java
// 方式一
private final String POINT_CUT = "execution(* cn.middle.springbt_mybatis_mysql.controller.*.*(..))";

@Pointcut(POINT_CUT)
public void weblog(){
    
}
```

```java
// 方式二
@Pointcut("execution(* cn.middle.springbt_mybatis_mysql.controller.*.*(..))")//controller包中的所有类所有方法(包含的参数任意)
public void weblog() {
    
}
```

### 通知

Advice，通知增强，主要包括五个注解**@Before**,**@After**,**@AfterReturning**,**@AfterThrowing**,**@Around**

```java
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
//如果定义切点用的方式一，@AfterReturning(pointcut = POINT_CUT,returning = "res")
//pointcut可以换成value
public void doAfterReturningAdvice1(JoinPoint joinPoint,Object res){
logger.info("@AfterReturning后置返回通知的返回值："+res);
}
```


各种通知执行顺序
```java
try{
    try{
        //@Before
        method.invoke(..);
    }finally{
        //@After
    }
    //@AfterReturning
}catch(){
    //@AfterThrowing
}
```

```java
: @Around环绕通知
: @Before前置通知执行
: @Before通知执行结束
: @Around环绕通知执行结束
: @After后置通知执行
: @AfterReturning后置通知执行
```


### 切点表达式
- **execution**(方法修饰符 返回类型 方法全限定名(参数))         主要用来匹配整个方法签名和返回值的

```
"execution(public * com.xhx.springboot.controller.*.*(..))"
```
    *只能匹配一级路径  

    ..可以匹配多级，可以是包路径，也可以匹配多个参数

    +只能放在类后面，表明本类及所有子类

   还可以按下面这么玩，所有get开头的，第一个参数是Long类型的
```
@Pointcut("execution(* *..get*(Long,..))")
```


- **within**(类路径)   用来限定类，同样可以使用匹配符

下面用来表示com.middle.springboot包及其子包下的所有类方法
```
"within(cn.middle.springboot..*)"
```


- 可以使用&&、||、!、三种运算符来**组合切点表达式**，表示与或非的关系。

@Around(value = "pointcut1() || pointcut2()")


- 更多用法，请百度**切点表达式**

