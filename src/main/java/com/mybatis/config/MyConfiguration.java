package com.mybatis.config;

import com.mybatis.model.InterfaceModel;
import com.mybatis.model.MapperModel;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 文件读取配置配置类
 */
public class MyConfiguration {
    private ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    private Properties prop;
    private Map<String,InterfaceModel> mappers = new HashMap<String,InterfaceModel>();
    /**
     * 构造对象读取全局配置问价内容
     *    jdbc 配置读取
     *    mappers 映射文件内容读取
     * @param file
     */
    public MyConfiguration(String file) {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(classLoader.getResourceAsStream(file));
            // 加载properties 配置信息
            this.loadProperties(document);
            // 加载Mapper 映射文件信息到mappers key 为映射文件命名空间
            this.loadMapper(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载Mapper 映射文件信息
     * @param document
     */
    private void loadMapper(Document document) {
        XPath xPath = document.createXPath("//configuration/mappers/mapper");
        List<Element> mapperList = xPath.selectNodes(document);
        mapperList.forEach(mapper->{
            // 获取映射文件classpath 路径
            String mapperPath = mapper.attributeValue("resource");
            SAXReader reader = new SAXReader();
            try {
                Document mapperDoc = reader.read(classLoader.getResourceAsStream(mapperPath));
                Element element = mapperDoc.getRootElement();
                // 构造InterfaceModel 对象 存储映射文件标签属性与文本内容
                final InterfaceModel ifm = new InterfaceModel();
                ifm.setInterfaceName(element.attributeValue("namespace"));
                // 读取命名空间
                List<Element> elementList = element.elements();
                elementList.forEach(e -> {
                    MapperModel mapperModel = new MapperModel();
                    mapperModel.setId(e.attributeValue("id"));
                    mapperModel.setParameterType(e.attributeValue("parameterType"));
                    mapperModel.setResultType(e.attributeValue("resultType"));
                    mapperModel.setSql(e.getText().trim());
                    ifm.getMethods().add(mapperModel);
                });
                // 将每个映射文件对应InterfaceModel 存入map
                mappers.put(ifm.getInterfaceName(),ifm);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 加载properties 配置信息
     * @param document
     */
    private void loadProperties(Document document) {
        try {
            XPath xPath = document.createXPath("//configuration/property[1]");
            Element element= (Element) xPath.selectSingleNode(document);
            prop =new Properties();
            prop.load(classLoader.getResourceAsStream(element.attributeValue("resource")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取配置文件构建Connector 对象
     */
    public Connection  getConnection(){
        Connection connection=null;
        try {
            //链接数据库
            Class.forName(prop.getProperty("jdbc.driver"));
            connection = DriverManager.getConnection(prop.getProperty("jdbc.url"),
                    prop.getProperty("jdbc.userName"), prop.getProperty("jdbc.password"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }


    public InterfaceModel getInterfaceModel(String namespace){
       return  mappers.get(namespace);
    }




 /*   public static void main(String[] args) {
        MyConfiguration configuration =new MyConfiguration("mybatis.xml");
        configuration.getMappers().forEach((k,v)->{
            System.out.println("接口路径-->"+k);
            System.out.println("接口方法配置信息->"+v);
        });
    }
*/
}
