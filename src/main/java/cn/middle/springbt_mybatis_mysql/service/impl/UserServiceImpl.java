package cn.middle.springbt_mybatis_mysql.service.impl;

import cn.middle.springbt_mybatis_mysql.entity.User;
import cn.middle.springbt_mybatis_mysql.mapper.UserMapper;
import cn.middle.springbt_mybatis_mysql.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Primary  // 注意这个注解的添加，否则会报：Autowired required a single bean, but 2 were found！！！ （疑问？难道UserServiceImpl和IUserService不是一个东西嘛？）
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
