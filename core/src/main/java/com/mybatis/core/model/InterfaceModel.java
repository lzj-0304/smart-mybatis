package com.mybatis.core.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InterfaceModel {
    private String interfaceName;// 接口名
    private List<MapperModel> methods = new ArrayList<MapperModel>();// 接口方法
}
