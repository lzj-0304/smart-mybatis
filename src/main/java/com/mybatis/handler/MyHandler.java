package com.mybatis.handler;

import com.mybatis.config.MyConfiguration;
import com.mybatis.session.MySqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyHandler implements InvocationHandler {

    private MyConfiguration configuration;
    private MySqlSession sqlSession;

    public MyHandler(MyConfiguration configuration) {
        this.configuration = configuration;
        sqlSession =new MySqlSession(configuration);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("接口路径-->"+method.getDeclaringClass().getName());
        /*InterfaceModel ifm = configuration.parseMapper();
        if (!ifm.getInterfaceName().equals(method.getDeclaringClass().getName())) {
            return null;
        }*/
        // 执行查询获取结果返回
     /*   List<MapperModel> mapperModels = ifm.getMethods();
        for(MapperModel mapperModel:mapperModels){
            if(mapperModel.getId().equals(method.getName())){
                System.out.println("执行查询操作。。。");
                return sqlSession.selectOne(mapperModel.getSql(),args[0]);
            }
        }*/
        return null;
    }
}


