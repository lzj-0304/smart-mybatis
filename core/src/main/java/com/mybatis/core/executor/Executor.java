package com.mybatis.core.executor;

public interface Executor {

    /**
     * 查询单条记录
     * @param statement
     * @param parameters
     * @param <T>
     * @return
     */
    <T> T selectOne(String statement, Object... parameters);
}
