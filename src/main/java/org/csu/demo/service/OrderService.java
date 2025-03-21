package org.csu.demo.service;

import org.csu.demo.persistence.mappers.OrderMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("orderService")
@MapperScan("org.csu.demo.persistence.mappers")
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

}
