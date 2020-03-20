package com.xch.service.impl;

import com.xch.annotation.ExtResource;
import com.xch.annotation.ExtService;
import com.xch.service.OrderService;
import com.xch.service.UserService;

/**
 * @author xiech
 * @create 2020-01-17 16:36
 */
@ExtService
public class UserServiceImpl implements UserService {
    @ExtResource
    private OrderService orderServiceImpl;

    @Override
    public void add(){
        System.out.println("add");
        orderServiceImpl.addOrder();
    }
}
