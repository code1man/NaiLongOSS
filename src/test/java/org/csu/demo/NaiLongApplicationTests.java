package org.csu.demo;

import org.csu.demo.domain.ProductType;
import org.csu.demo.domain.User;
import org.csu.demo.persistence.BusinessDao;
import org.csu.demo.persistence.ItemDao;
import org.csu.demo.persistence.UserDao;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@MapperScan("org.csu.demo.persistence")
class NaiLongApplicationTests {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BusinessDao businessDao;
    @Qualifier("itemDao")
    @Autowired
    private ItemDao itemDao;

    @Test
    void test1(){
        System.out.println(userDao);
        User user = userDao.getUserByUsernameAndPassword("zkd", "123");
        System.out.println(user);
    }

    @Test
    void test2() {
        User user = User.builder().build();
        user.setUsername("AnyCode");
        user.setAge(20);

        User student = User.builder()
                .username("张三")
                .age(12)
                .build();

        System.out.println(user.toString());
        System.out.println(student.toString());
    }

    @Test
    void test3() {
/*        System.out.println(ProductType.fromIndex(9));*/
//        System.out.println(itemDao.getItem(10101));
    }


    @Test
    void test4() {
        User user = new User();
        user.setUsername("esrwer");
        user.setPassword("123");
        user.setAge(20);
        user.setEmail("123@123.com");
        user.setAdmin(true);
        System.out.println(userDao.addUser(user));
    }

}
