package cn.middle.springbt_mybatis_mysql.controller;

import cn.middle.springbt_mybatis_mysql.entity.User;
import cn.middle.springbt_mybatis_mysql.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/getAllUser", method = RequestMethod.GET)
    public List<User> getAllUser() {
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public int addUser( @RequestBody User user ) {
        return userService.addUser( user );
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.DELETE)
    public int deleteUser( @RequestBody User user ) {
        return userService.deleteUser( user );
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.PUT)
    public int updateUser( @RequestBody User user ) {
        return userService.updateUser( user );
    }
    // 这里前端传递的内容都是json
    // 注解@RequestParam接收的参数是来自requestHeader中，即请求头。通常用于GET请求
    // 注解@RequestBody接收的参数是来自requestBody中，即请求体。

    // 注解@RequestParam与@RequestBody的使用场景,参考
    // https://cloud.tencent.com/developer/article/1414464
}
