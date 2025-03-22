package org.csu.demo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.demo.domain.Order;
import org.csu.demo.domain.User;
import org.csu.demo.persistence.BusinessDao;
import org.csu.demo.persistence.mappers.OrderMapper;
import org.csu.demo.persistence.mappers.UserMapper;
import org.csu.demo.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

@SpringBootTest
@ActiveProfiles("test")  // 启用 test 这个 Profile
@MapperScan("org.csu.demo.persistence")
public class OrderTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BusinessDao businessDao;

    @Test
    void test10096(){
        System.out.println(userMapper);
        System.out.println(orderMapper);
    }
    @Test
    void test(){
        User user = userMapper.selectById(1);
        System.out.println(user);
    }

    @Test
    void test1(){
        System.out.println(businessDao.getSupplierByItemId(10101));
    }

    @Test
    void test2(){
        User user = userMapper.selectById(1);
        Order order = new Order("order1", user.getId(), 1, 10101, 1, 100, 10086,
                0,new Date() , null, null, null, null, null, "");
        orderMapper.insert(order);
    }

    @Test
    void test3(){
        System.out.println(orderService.getOrderListByClient(1,0));
        System.out.println(orderService.getOrderListByClient(4,1));
    }


}
