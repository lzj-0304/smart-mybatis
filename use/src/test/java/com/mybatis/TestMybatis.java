package com.mybatis;

import com.mybatis.core.config.MyConfiguration;
import com.mybatis.core.session.MySqlSession;
import com.mybatis.dao.UserMapper;
import org.junit.Test;

public class TestMybatis {
    @Test
    public void test01(){
        MyConfiguration configuration = new MyConfiguration("mybatis.xml");
        MySqlSession session = new MySqlSession(configuration);
        UserMapper userMapper =session.getMapper(UserMapper.class);
        System.out.println(userMapper.queryUserById(46));
    }

    @Test
    public void test02(){
        MyConfiguration configuration = new MyConfiguration("mybatis.xml");
        MySqlSession session = new MySqlSession(configuration);
        UserMapper userMapper =session.getMapper(UserMapper.class);
        userMapper.queryUsers().forEach(System.out::println);
    }
}
