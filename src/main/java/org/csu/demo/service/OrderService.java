package org.csu.demo.service;

import lombok.extern.log4j.Log4j2;
import org.csu.demo.persistence.mappers.OrderMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("orderService")
@Log4j2
@MapperScan("org.csu.demo.persistence.mappers")
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

}
