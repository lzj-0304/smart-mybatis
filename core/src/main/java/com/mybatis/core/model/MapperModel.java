package com.mybatis.core.model;


import lombok.Data;

@Data
public class MapperModel {
    private String id;
    private String parameterType;
    private String resultType;
    private String sql;

}
