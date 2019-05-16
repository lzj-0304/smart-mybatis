package com.mybatis.session;

import com.mybatis.config.MyConfiguration;
import com.mybatis.dao.UserMapper;
import com.mybatis.executor.Executor;
import com.mybatis.executor.MyBaseExecutor;
import com.mybatis.handler.MyHandler;
import java.lang.reflect.Proxy;

public class MySqlSession {
    private MyConfiguration configuration;
    private Executor executor;

    public MySqlSession(MyConfiguration configuration) {
        this.configuration = configuration;
        executor = new MyBaseExecutor(configuration);
    }

    /**
     * 执行查询
     *
     * @param statement
     * @param parameters
     * @param <T>
     * @return
     */
    public <T> T selectOne(String statement, Object... parameters) {
        return executor.selectOne(statement, parameters);
    }


    /**
     * 获取接口代理类
     *
     * @param clazz
     * @param <T>
     * @return
     */


    public <T> T getMapper(Class clazz) {
        return (T) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                new Class[]{clazz}, new MyHandler(configuration));
    }


    public static void main(String[] args) {
        MyConfiguration configuration =new MyConfiguration("mybatis.xml");
        UserMapper userMapper = new MySqlSession(configuration).getMapper(UserMapper.class);
        System.out.println(userMapper);
        System.out.println(userMapper.queryUserById(46));
    }
}

