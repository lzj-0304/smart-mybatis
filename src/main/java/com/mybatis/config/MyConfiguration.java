package com.mybatis.config;

import com.mybatis.model.InterfaceModel;
import com.mybatis.model.MapperModel;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;

/**
 * 文件读取配置配置类
 */
public class MyConfiguration {
    static  ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    /**
     * 读取配置文件构建Connector 对象
     */
    public Connection  getConnection(String fileName){
        /**
         * 解析指定标签
         */
        Connection connection=null;
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(classLoader.getResourceAsStream(fileName));
            XPath xPath =  document.createXPath("//configuration/property[1]");
            Element element= (Element) xPath.selectSingleNode(document);
            Properties prop =new Properties();
            prop.load(classLoader.getResourceAsStream(element.attributeValue("resource")));
            //链接数据库
            Class.forName(prop.getProperty("jdbc.driver"));
            connection = DriverManager.getConnection(prop.getProperty("jdbc.url"),
                    prop.getProperty("jdbc.userName"), prop.getProperty("jdbc.password"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 读取mapper 文件 到MapperModel 中
     * @param mapperPath    mapper路径
     * @return
     */
    public InterfaceModel parseMapper(String mapperPath){
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(classLoader.getResourceAsStream(mapperPath));
            Element element =  document.getRootElement();
            List<Element> elementList =  element.elements();
            // 读取命名空间
            String interfaceName = element.attributeValue("namespace");
            final InterfaceModel ifm = new InterfaceModel();
            ifm.setInterfaceName(interfaceName);
            // 读取字标签select
            elementList.forEach(e->{
                MapperModel mapperModel = new MapperModel();
                mapperModel.setId(e.attributeValue("id"));
                mapperModel.setParameterType(e.attributeValue("parameterType"));
                mapperModel.setResultType(e.attributeValue("resultType"));
                mapperModel.setSql(e.getText().trim());
                ifm.getMethods().add(mapperModel);
            });
            return ifm;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        MyConfiguration configuration =new MyConfiguration();
        //configuration.getConnection("mybatis.xml");
        InterfaceModel ifm = configuration.parseMapper("mappers/UserMapper.xml");
        System.out.println(ifm.getInterfaceName());
        ifm.getMethods().forEach(e->{
            System.out.println(e);
        });



    }

}
