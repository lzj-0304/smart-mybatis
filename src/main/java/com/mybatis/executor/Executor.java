package com.mybatis.executor;

public interface Executor {

    /**
     * 查询单条记录
     * @param sql
     * @param parameter
     * @param <T>
     * @return
     */
    <T> T selectOne(String sql,Object parameter);
}
