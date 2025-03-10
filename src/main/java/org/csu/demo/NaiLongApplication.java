package org.csu.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.csu.demo.persistence")
public class NaiLongApplication {

    public static void main(String[] args) {
        SpringApplication.run(NaiLongApplication.class, args);
    }

}
