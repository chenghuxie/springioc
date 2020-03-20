package com.xch.test;


import com.xch.service.UserService;
import com.xch.springxml.ExtClassPathXmlApplicationContext;

/**
 * @author xiech
 * @create 2020-01-17 13:37
 */
public class Test01 {
    public static void main(String[] args) throws Exception {
        ExtClassPathXmlApplicationContext applicationContext = new ExtClassPathXmlApplicationContext("com.xch.service.impl");
        UserService userService= (UserService) applicationContext.getBean("userServiceImpl");
        userService.add();
    }
}
