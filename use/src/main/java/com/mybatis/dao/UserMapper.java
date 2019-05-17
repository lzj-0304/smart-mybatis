package com.mybatis.dao;

import com.mybatis.pojo.User;

import java.util.List;

public interface UserMapper {
    public User queryUserById(Integer id);

    public User queryUserByUserName(String userName);

    public List<User> queryUsers();
}
