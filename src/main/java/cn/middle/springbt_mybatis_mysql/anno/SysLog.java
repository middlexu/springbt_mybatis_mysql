package cn.middle.springbt_mybatis_mysql.anno;

import java.lang.annotation.*;

/**
 * 定义系统日志注解
 * @author 15510
 * @create 2019-06-20 22:21
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    String value() default "";
}
