package com.mybatis.core.handler;


import com.mybatis.core.config.MyConfiguration;
import com.mybatis.core.model.InterfaceModel;
import com.mybatis.core.model.MapperModel;
import com.mybatis.core.session.MySqlSession;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class MyHandler implements InvocationHandler {
    private MyConfiguration configuration;
    public MyHandler(MyConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 获取statement 对应 接口路径
        InterfaceModel ifm = configuration.getInterfaceModel(method.getDeclaringClass().getName());
        if(null != ifm){
            // 执行查询获取结果返回
            List<MapperModel> mapperModels = ifm.getMethods();
            for(MapperModel mapperModel:mapperModels){
                if(mapperModel.getId().equals(method.getName())){
                    System.out.println("执行查询操作。。。");
                    String statement = ifm.getInterfaceName()+"."+method.getName();
                    System.out.println("statement-->"+statement);
                    switch (mapperModel.getSqlType()){
                        case INSERT:
                        case UPDATE:
                        case DELETE:
                            return new MySqlSession(configuration).update(statement,args);
                        case SELECT:
                            return new MySqlSession(configuration).selectList(statement,args);
                        default:
                            return null;
                    }
                }
            }
        }
        return null;
    }
}


