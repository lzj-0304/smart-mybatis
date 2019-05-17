package com.mybatis.core.model;


import com.mybatis.core.enums.SqlType;
import lombok.Data;

@Data
public class MapperModel {
    private String id;
    private String parameterType;
    private String resultType;
    private String sql;
    private SqlType sqlType;

}
