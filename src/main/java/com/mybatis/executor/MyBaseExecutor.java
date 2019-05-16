package com.mybatis.executor;


import com.mybatis.config.MyConfiguration;
import com.mybatis.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyBaseExecutor implements Executor {
    private MyConfiguration configuration =new MyConfiguration();


    /**
     * 仅限参数为1个情况  列固定
     * @param sql
     * @param parameter
     * @param <T>
     * @return
     */
    @Override
    public <T> T selectOne(String sql, Object parameter) {
        Connection connection =null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user =null;
        try {
            connection=configuration.getConnection("mybatis.xml");
            ps =  connection.prepareStatement(sql);
            ps.setObject(1,parameter);
            rs = ps.executeQuery();
            user = new User();
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("userName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (rs!=null) {
                    rs.close();
                }
                if (ps!=null) {
                    ps.close();
                }
                if (connection!=null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return (T)user;
    }




}
