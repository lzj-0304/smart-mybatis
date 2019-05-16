package com.mybatis.session;

import com.mybatis.config.MyConfiguration;
import com.mybatis.dao.UserMapper;
import com.mybatis.executor.Executor;
import com.mybatis.executor.MyBaseExecutor;
import com.mybatis.handler.MyHandler;

import java.lang.reflect.Proxy;

public class MySqlSession {
    private Executor executor = new MyBaseExecutor();
    /**
     * 执行查询
     * @param sql
     * @param parameter
     * @param <T>
     * @return
     */
    public <T> T selectOne(String sql,Object parameter ){
        return executor.selectOne(sql,parameter);
    }
    /**
     * 获取接口代理类
     * @param clazz
     * @param <T>
     * @return
     */
    public  <T> T getMapper(Class clazz){
        return (T) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                new Class[]{clazz},new MyHandler(new MyConfiguration(),this));
    }


    public static void main(String[] args) {
       UserMapper userMapper= new MySqlSession().getMapper(UserMapper.class);
        System.out.println(userMapper);
        System.out.println(userMapper.queryUserById(46));
    }
}
