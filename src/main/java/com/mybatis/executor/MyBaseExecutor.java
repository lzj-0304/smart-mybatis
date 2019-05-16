
package com.mybatis.executor;


import com.mybatis.config.MyConfiguration;
import com.mybatis.model.InterfaceModel;
import com.mybatis.model.MapperModel;
import com.mybatis.pojo.User;
import com.mybatis.utils.DBUtil;
import com.mybatis.utils.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class MyBaseExecutor implements Executor {
    private MyConfiguration configuration;

    public MyBaseExecutor(MyConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     *
     * @param statement   namespace+id
     * @param parameters
     * @param <T>
     * @return
     */
    @Override
    public <T> T selectOne(String statement, Object... parameters) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Object result =null;
        try {
            connection = configuration.getConnection();
            // 获取namespace
            String namespace = statement.substring(0,statement.lastIndexOf("."));
            String method = statement.substring(statement.lastIndexOf(".")+1);
            InterfaceModel ifm = configuration.getInterfaceModel(namespace);
            for(MapperModel mapperModel:ifm.getMethods()){
                if(mapperModel.getId().equals(method)){
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
                    result = this.processResult(rs,Class.forName(mapperModel.getResultType()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs,ps,connection);
        }
        return (T) result;
    }

    /**
     * 收集结果
     * @param rs
     * @param clz
     * @param <T>
     * @return
     */
    private <T> T processResult(ResultSet rs, Class<?> clz) {
        Object result = null;
        try {
            // 元结果集 获取字段个数和字段名字 结果的描述
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                // 创建一个对象
                result = clz.newInstance();
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) result;
    }


    public static void main(String[] args) throws ClassNotFoundException {
       MyConfiguration configuration =new MyConfiguration("mybatis.xml");
       Executor executor =new MyBaseExecutor(configuration);
       User user= executor.selectOne("com.mybatis.dao.UserMapper.queryUserById",46);
        System.out.println(user);
    }

}

