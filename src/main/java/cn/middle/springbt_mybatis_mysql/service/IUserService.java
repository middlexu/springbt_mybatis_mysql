package cn.middle.springbt_mybatis_mysql.service;

import cn.middle.springbt_mybatis_mysql.entity.User;

import java.util.List;

public interface IUserService {

    List<User> getAllUsers();
    int addUser( User user );
    int deleteUser( User user );
}
