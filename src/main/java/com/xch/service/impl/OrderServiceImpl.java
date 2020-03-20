package com.xch.service.impl;

import com.xch.annotation.ExtService;
import com.xch.service.OrderService;

/**
 * @author xiech
 * @create 2020-01-19 10:41
 */
@ExtService
public class OrderServiceImpl implements OrderService {

    @Override
    public void addOrder() {
        System.out.println("dingdan");
    }
}
