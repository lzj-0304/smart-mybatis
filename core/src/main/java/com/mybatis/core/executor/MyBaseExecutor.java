
package com.mybatis.core.executor;


import com.mybatis.core.config.MyConfiguration;
import com.mybatis.core.model.InterfaceModel;
import com.mybatis.core.model.MapperModel;
import com.mybatis.core.utils.DBUtil;
import com.mybatis.core.utils.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyBaseExecutor implements Executor {
    private MyConfiguration configuration;

    public MyBaseExecutor(MyConfiguration configuration) {
        this.configuration = configuration;
    }


    @Override
    public <T> List<T> selectList(String statement, Object... parameters) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> results = null;
        try {
            connection = configuration.getConnection();
            // 获取namespace
            String namespace = statement.substring(0, statement.lastIndexOf("."));
            String method = statement.substring(statement.lastIndexOf(".") + 1);
            InterfaceModel ifm = configuration.getInterfaceModel(namespace);
            for (MapperModel mapperModel : ifm.getMethods()) {
                if (mapperModel.getId().equals(method)) {
                    ps = connection.prepareStatement(mapperModel.getSql());
                    if (parameters != null && parameters.length > 0) {
                        for (int i = 0; i < parameters.length; i++) {
                            // 循环设置参数
                            ps.setObject(i + 1, parameters[i]);
                        }
                    }
                    // 执行查询
                    rs = ps.executeQuery();
                    //处理结果
                    results = this.processResult(rs, Class.forName(mapperModel.getResultType()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, connection);
        }
        return results;
    }

    /**
     * 数据更新
     * @param statement
     * @param parameters
     * @return
     */
    @Override
    public int update(String statement, Object... parameters) {
        Connection connection = null;
        PreparedStatement ps = null;
        int row = 0;
        try {
            // 得到数据库连接
            connection = configuration.getConnection();
            // 设置事务不自动提交
            connection.setAutoCommit(false);
            // 获取namespace
            String namespace = statement.substring(0, statement.lastIndexOf("."));
            String method = statement.substring(statement.lastIndexOf(".") + 1);
            InterfaceModel ifm = configuration.getInterfaceModel(namespace);
            for (MapperModel mapperModel : ifm.getMethods()) {
                if (mapperModel.getId().equals(method)) {
                    ps = connection.prepareStatement(mapperModel.getSql());
                    if (parameters != null && parameters.length > 0) {
                        for (int i = 0; i < parameters.length; i++) {
                            // 循环设置参数
                            ps.setObject(i + 1, parameters[i]);
                        }
                    }
                    // 执行更新
                    row = ps.executeUpdate();
                    connection.commit();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 事务回滚
            try {
                connection.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            DBUtil.close(null, ps, connection);
        }
        return row;
    }

    /**
     * 收集结果
     *
     * @param rs
     * @param clz
     * @param <T>
     * @return
     */
    private <T> List<T> processResult(ResultSet rs, Class<?> clz) {
        List results = new ArrayList();
        try {
            // 元结果集 获取字段个数和字段名字 结果的描述
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                // 创建一个对象
                Object result = clz.newInstance();
                // 给对象设置属性值
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    // 获取属性的名字， 字段的名字（别名）
                    String fieldName = rsmd.getColumnLabel(i + 1);
                    // 通过反射，通过指定列名得到实体类中的具体属性，获取某个属性 name
                    Field fild = clz.getDeclaredField(fieldName);
                    // 通过反射，拼接实体类中的set方法，并设置指定类型， set + 列名（列名首字母大写） name setName  fild.getType()：属性的类型
                    Method method = clz.getDeclaredMethod("set" + StringUtil.firstChar2Upper(fieldName),
                            fild.getType());
                    // 将method放到newInstance()之后的对象中，并且将列名对应的结果设置到方法中
                    method.invoke(result, rs.getObject(fieldName));
                }
                results.add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

}

