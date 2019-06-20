package cn.middle.springbt_mybatis_mysql;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("cn.middle.springbt_mybatis_mysql")
public class SpringbtMybatisMysqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbtMybatisMysqlApplication.class, args);
    }
}
