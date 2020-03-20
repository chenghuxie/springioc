package com.xch.springxml;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


import java.io.InputStream;
import java.util.List;

/**
 *
 * sping ioc  xml形式
 * @author xiech
 * @create 2020-01-17 16:41
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ClassPathXmlApplicationContext {
    private String xmlPath;

    public ClassPathXmlApplicationContext(String xmlPath){
        this.xmlPath=xmlPath;
    }

    public Object getBean(String beanId) throws Exception {
        //1 读取配置文件
      List<Element> elements=readerXml();
      if(elements==null){
          throw new Exception("该配置文件没有子元素");
      }
      //2 使用beanid 查找对应的class地址
        String classPath = findxmlByClassId(elements, beanId);

      //3 反射初始化对象
        Class<?> aClass = Class.forName(classPath);
        return aClass.newInstance();
    }

    private String findxmlByClassId(List<Element> elements, String beanId) throws Exception {
        for (Element element : elements) {
            // 读取节点上是否有value
            String beanIdValue = element.attributeValue("id");
            if (beanIdValue == null) {
                throw new Exception("使用该beanId为查找到元素");
            }
            if (!beanIdValue.equals(beanId)) {
                continue;
            }
            // 获取Class地址属性
            String classPath = element.attributeValue("class");
            if (!StringUtils.isEmpty(classPath)) {
                return classPath;
            }
        }
        return null;
    }

    /**
     *读取xml获取文件信息
     * @return
     */
    private  List<Element> readerXml() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        if (StringUtils.isEmpty(xmlPath)) {
            new Exception("xml路径为空...");
        }
        Document read = saxReader.read(getClassXmlInputStream(xmlPath));
        // 获取根节点信息
        Element rootElement = read.getRootElement();
        // 获取子节点
        List<Element> elements = rootElement.elements();
        if (elements == null || elements.isEmpty()) {
            return null;
        }
        return elements;
    }

    /**
     *读取xml配置文件
     * @param xmlPath
     * @return
     */
    private InputStream getClassXmlInputStream(String xmlPath) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(xmlPath);
        return inputStream;
    }

}
