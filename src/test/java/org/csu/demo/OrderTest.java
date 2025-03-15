package org.csu.demo;

import org.csu.demo.domain.User;
import org.csu.demo.persistence.mappers.UserMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@MapperScan("org.csu.demo.mappers")
public class OrderTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void test(){
        User user = userMapper.selectById(1);
        System.out.println(user);
    }
}
