package com.mybatis.handler;

import com.mybatis.config.MyConfiguration;
import com.mybatis.model.InterfaceModel;
import com.mybatis.model.MapperModel;
import com.mybatis.session.MySqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class MyHandler implements InvocationHandler {

    private MyConfiguration configuration;
    private MySqlSession sqlSession;

    public MyHandler(MyConfiguration configuration, MySqlSession sqlSession) {
        this.configuration = configuration;
        this.sqlSession = sqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        InterfaceModel ifm = configuration.parseMapper("mappers/UserMapper.xml");
        if (!ifm.getInterfaceName().equals(method.getDeclaringClass().getName())) {
            return null;
        }
        // 执行查询获取结果返回
        List<MapperModel> mapperModels = ifm.getMethods();
        for(MapperModel mapperModel:mapperModels){
            if(mapperModel.getId().equals(method.getName())){
                System.out.println("执行查询操作。。。");
                return sqlSession.selectOne(mapperModel.getSql(),args[0]);
            }
        }
        return null;
    }
}
