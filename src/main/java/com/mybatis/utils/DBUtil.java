package com.mybatis.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBUtil {
    /**
     * 关闭数据库连接
     * @param resultSet
     * @param preparedStatement
     * @param connection
     */
    public static void close(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection){
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {

        }
    }
}
