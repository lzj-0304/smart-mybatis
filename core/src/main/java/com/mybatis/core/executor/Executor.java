package com.mybatis.core.executor;

import java.util.List;

public interface Executor {

    /**
     * 查询List 数据
     * @param statement
     * @param parameters
     * @param <T>
     * @return
     */
    <T> List<T> selectList(String statement, Object... parameters);




}
