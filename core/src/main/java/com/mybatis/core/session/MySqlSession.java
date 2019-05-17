package com.mybatis.core.session;


import com.mybatis.core.config.MyConfiguration;
import com.mybatis.core.executor.Executor;
import com.mybatis.core.executor.MyBaseExecutor;
import com.mybatis.core.handler.MyHandler;

import java.lang.reflect.Proxy;
import java.util.List;

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
     * @return
     */
    public <T> List<T> selectList(String statement, Object... parameters) {
        return executor.selectList(statement, parameters);
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



}

