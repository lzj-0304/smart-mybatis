package com.mybatis.dao;

import com.mybatis.pojo.User;

public interface UserMapper {
    public User queryUserById(Integer id);
}
