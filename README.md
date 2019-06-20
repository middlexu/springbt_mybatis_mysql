# Spring Boot集成 MyBatis和 MySQL实践》

---

> 概 述

Spring Boot工程集成 MyBatis来实现 MySQL访问的示例我们见过很多，于是本文则给出一个完整的 **Spring Boot + MyBatis + MySQL** 的工程示例。



---

## 工程结构

![img](https://mmbiz.qpic.cn/mmbiz_png/9Z6Ueb7N5CQFplOr8MTOyLksN7KpWoaOY5r1um45hncwpIaNl7wxB2qCmuxCQ8wYOmqXT5gUWiaFE3MSqmQBcgw/640?wx_fmt=png)



---

## 工程搭建

- 新建 Spring Boot工程
- `pom.xml` 中添加 MyBatis和 SQL Server相关的依赖

```xml
<!--for mybatis-->
<dependency>
<groupId>org.mybatis.spring.boot</groupId>
<artifactId>mybatis-spring-boot-starter</artifactId>
<version>1.3.2</version>
</dependency>

<!--for MySQL-->
<dependency>
   <groupId>mysql</groupId>
   <artifactId>mysql-connector-java</artifactId>
</dependency>

<!-- druid -->
<dependency>
   <groupId>com.alibaba</groupId>
   <artifactId>druid</artifactId>
   <version>1.1.10</version>
</dependency>
```

- 配置 `application.properties`或者`application.yml`

这里同样主要是对于 MyBatis 和MySQL连接相关的配置

```properties
server.port=80

# mybatis 配置
mybatis.type-aliases-package=cn.middle.springbt_mybatis_mysql.entity
mybatis.mapper-locations=classpath:mapper/*.xml
mybatis.configuration.map-underscore-to-camel-case=true


## MySQL 数据源配置
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&amp;characterEncoding=utf-8
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root
```



---

## 建立MySQL数据表和实体类

- 首先在 My数据库中新建数据表 `user_test`作为测试用表

```mysql
CREATE TABLE `test`.`user_test` (
 `user_id` int(10) NOT NULL,
 `user_name` varchar(255) NULL,
 `sex` tinyint(1) NULL,
 `created_time` timestamp DEFAULT CURRENT_TIMESTAMP,
 PRIMARY KEY (`user_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

- 然后在我们的工程中对应建立的 `User`实体类

其字段和实际数据表的字段一一对应

```java
public class User {

   private Long userId;
   private String userName;
   private Boolean sex;
   private String createdTime;

   public Long getUserId() {
       return userId;
  }

   public void setUserId(Long userId) {
       this.userId = userId;
  }

   public String getUserName() {
       return userName;
  }

   public void setUserName(String userName) {
       this.userName = userName;
  }

   public Boolean getSex() {
       return sex;
  }

   public void setSex(Boolean sex) {
       this.sex = sex;
  }

   public String getCreatedTime() {
       return createdTime;
  }

   public void setCreatedTime(String createdTime) {
       this.createdTime = createdTime;
  }
}
```



---

## Mybatis Mapper映射配置

- MyBatis映射配置的 `UserMapper.xml`文件如下：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="cn.middle.springbt_mybatis_mysql.mapper.UserMapper">

   <resultMap id="userMap" type="cn.middle.springbt_mybatis_mysql.entity.User">
       <id property="userId" column="user_id" javaType="java.lang.Long"></id>
       <result property="userName" column="user_name" javaType="java.lang.String"></result>
       <result property="sex" column="sex" javaType="java.lang.Boolean"></result>
       <result property="createdTime" column="created_time" javaType="java.lang.String"></result>
   </resultMap>

   <select id="getAllUsers" resultMap="userMap">
      select * from user_test
   </select>

   <insert id="addUser" parameterType="cn.middle.springbt_mybatis_mysql.entity.User">
      insert into user_test ( user_id, user_name, sex, created_time ) values ( #{userId}, #{userName}, #{sex}, #{createdTime} )
   </insert>

   <delete id="deleteUser" parameterType="cn.middle.springbt_mybatis_mysql.entity.User">
      delete from user_test where user_name = #{userName}
   </delete>

</mapper>
```

- 与此同时，这里也给出对应 XML的 DAO接口

```java
public interface UserMapper {
   List<User> getAllUsers();
   int addUser( User user );
   int deleteUser( User user );
}
```

为了试验起见，这里给出了 **增 / 删 / 查** 三个数据库操作动作。





---

## 编写 Service 和测试Controller

- 上面这些准备工作完成之后，接下来编写数据库 CRUD的 Service类

```java
@Service
@Primary
public class UserServiceImpl implements IUserService {

   @Autowired
   private UserMapper userMapper;

   @Override
   public List<User> getAllUsers() {
       return userMapper.getAllUsers();
  }

   @Override
   public int addUser(User user) {
       SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       user.setCreatedTime( form.format(new Date()) );
       return userMapper.addUser( user );
  }

   @Override
   public int deleteUser(User user) {
       return userMapper.deleteUser( user );
  }
}
```

这里的 Service功能同样主要关于数据表的 **增 / 删 / 查** 三个数据库操作动作。

- 对照着上面的Service，我们编写一个对应接口测试的Controller

```java
@RestController
public class UserController {

   @Autowired
   private IUserService userService;

   @RequestMapping(value = "/getAllUser", method = RequestMethod.GET)
   public List<User> getAllUser() {
       return userService.getAllUsers();
  }

   @RequestMapping(value = "/addUser", method = RequestMethod.POST)
   public int addUser( @RequestBody User user ) {//@RequestBody将前端传过来的json包装成User对象
       return userService.addUser( user );
  }

   @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
   public int deleteUser( @RequestBody User user ) {
       return userService.deleteUser( user );
  }

}
```



---

## 实验测试

- 插入数据

依次用 `Postman`通过 `Post /addUser`接口插入三条数据：

```
{"userId":1,"userName":"zhangsan","sex":true}
{"userId":2,"userName":"lisi","sex":false}
{"userId":3,"userName":"wangwu","sex":true}
```

![img](https://mmbiz.qpic.cn/mmbiz_png/9Z6Ueb7N5CQFplOr8MTOyLksN7KpWoaOsR2K2lk0F223VDNmT4kvwjnlO2mSWgjQBctVTJCSU2G8d1bIP2ND2g/640?wx_fmt=png)



插入完成后去 SQL Server数据库里看一下数据插入情况如下：

![img](https://mmbiz.qpic.cn/mmbiz_png/9Z6Ueb7N5CQFplOr8MTOyLksN7KpWoaOvmicxnIHHXibGgcaQUFKgUO5niaamm01NicIjib0fiaWzU7O68sLkVZVXVNg/640?wx_fmt=png)



- 查询数据

调用 `Get /getAllUser`接口，获取刚插入的几条数据

![img](https://mmbiz.qpic.cn/mmbiz_png/9Z6Ueb7N5CQFplOr8MTOyLksN7KpWoaOkEzwiacLzAYym5wesAeVBwfdALPBXcnyNPI3aIMcgJ8Vw5IIsesOzrw/640?wx_fmt=png)



- 删除数据

调用 `Post /deleteUser`接口，可以通过用户名来删除对应的用户

![img](https://mmbiz.qpic.cn/mmbiz_png/9Z6Ueb7N5CQFplOr8MTOyLksN7KpWoaOjn5UkRYJ3LG2JYsFqcTicXk3IkfUAVavmH1qUjCJdf951MKMiamBaz3A/640?wx_fmt=png)

刷新数据库，对象从数据库中删除了

![img](https://mmbiz.qpic.cn/mmbiz_png/9Z6Ueb7N5CQFplOr8MTOyLksN7KpWoaOGI9tPmjdlWPMdk1mibdXOnu6cbHIUWx1sAg2a04SjGtGfr4rIbkvaVg/640?wx_fmt=png)





---

## 后记

- 以上只是简单的实现了增删改查，还有很多需要完善。例如，AOP，事务控制等等