package cn.middle.springbt_mybatis_mysql.mapper;

import cn.middle.springbt_mybatis_mysql.entity.User;

import java.util.List;

public interface UserMapper {

    List<User> getAllUsers();
    int addUser( User user );
    int deleteUser( User user );
    int updateUser( User user);
}
