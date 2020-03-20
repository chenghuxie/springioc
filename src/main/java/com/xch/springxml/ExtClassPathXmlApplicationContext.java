package com.xch.springxml;

import com.xch.annotation.ExtResource;
import com.xch.annotation.ExtService;
import com.xch.utils.ClassUtil;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * sping ioc 注解形式
 *
 * IOC  DI  依赖注入
 * @author xiech
 * @create 2020-01-17 16:41
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ExtClassPathXmlApplicationContext {
    private String packageName;
    //bean容器
    private ConcurrentHashMap<String,Object> beans=null;

    public ExtClassPathXmlApplicationContext(String packageName) throws Exception {
        beans=new ConcurrentHashMap<>();
        this.packageName=packageName;
        initBeans();
        iterBeans();
    }

    /**
     * 遍历依赖注入属性  初始化
     * @throws Exception
     */
    private void iterBeans() throws Exception {
        //遍历
        for (Map.Entry<String,Object> entry : beans.entrySet()) {
            Object o=entry.getValue();
            attriAssign(o);
        }
    }

    /**
     * 扫包获取含有注解的class
     * @throws Exception
     */
    private void initBeans() throws Exception {
        List<Class<?>> classes=ClassUtil.getClasses(packageName);
        ConcurrentHashMap<String,Object> classExistAnnontation=findClassExistAnnotatian(classes);
        if(classExistAnnontation==null|| classExistAnnontation.isEmpty()){
            throw new Exception("该报下没有注解");
        }
    }

    /**
     * 根据beanId获取bean
     * @param beanId
     * @return
     * @throws Exception
     */
    public Object getBean(String beanId) throws Exception {
        if(StringUtils.isBlank(beanId)){
            throw new Exception("beanId不能为空");
        }

        Object bean=beans.get(beanId);
        return bean;
    }

    /**
     * 初始化bean
     * @param classInfo
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public  Object newInstance(Class<?> classInfo) throws IllegalAccessException, InstantiationException {
       return classInfo.newInstance();
    }


    //判断类上是否存在注入bean注解
    private ConcurrentHashMap<String,Object> findClassExistAnnotatian(List<Class<?>> classes) throws Exception {
        for (Class<?> clazz : classes) {
            ExtService extService = clazz.getAnnotation(ExtService.class);
            if(extService!=null){
               String className=clazz.getSimpleName();
               String beanId=toLowerCaseFirstOne(className);
               Object newInstance=newInstance(clazz);
               beans.put(beanId,newInstance);
            }
        }
        return beans;
    }
    //首字母转小写
    public static String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0))){
            return s;
        }else{
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }

    }

    /**
     * 依赖注入注解  判断是否含有
     * @param object
     */
    public void attriAssign(Object object) throws Exception {
     //使用反射机制，获取当前类的所有属性
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ExtResource extr = field.getAnnotation(ExtResource.class);
            if(extr!=null){
                String fieldName = field.getName();
                Object bean=getBean(fieldName);
                if(bean!=null){
                    field.setAccessible(true);
                    field.set(object,bean);
                }
            }
        }
    }




}
