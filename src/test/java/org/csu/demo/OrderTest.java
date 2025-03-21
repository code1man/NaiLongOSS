package org.csu.demo;

import com.google.gson.*;
import org.csu.demo.domain.User;
import org.csu.demo.persistence.mappers.UserMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@MapperScan("org.csu.demo.mappers")
@ActiveProfiles("test")  // 启用 test 这个 Profile
public class OrderTest {
    @Autowired
    private UserMapper userMapper;

    private static final String API_KEY = "sk-20d4b7f2ad0a4914b5dfea701736a765";
    private static final String BASE_URL = "https://api.deepseek.com";
    private static final String MODEL = "deepseek-chat";
    private RestTemplate restTemplate;

    @Test
    void test() {
        User user = userMapper.selectById(1);
        System.out.println(user);
    }


}

