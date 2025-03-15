package org.csu.demo;

import org.csu.demo.domain.Address;
import org.csu.demo.domain.Category;
import org.csu.demo.domain.Product;
import org.csu.demo.domain.User;
import org.csu.demo.persistence.*;
import org.csu.demo.service.CartService;
import org.csu.demo.service.CatalogService;
import org.csu.demo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
    @Autowired
    private CatalogService catalogService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    private AddressDao addressDao;

    @Test
    void testAddressDao() {
        List<Address> addressList = addressDao.getAllAddressById(48);
        for(Address address : addressList) {
            System.out.println(address);
        }
    }

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
                .password("123456")
                .age(12)
                .email("11@qq.com")
                .responsibility("管理员")
                .build();

        userDao.addUser(student);
        System.out.println(student.toString());
    }

    @Test
    void test3() {
        System.out.println(itemDao.getItem(10101));
        System.out.println(businessDao.getAllBusinessItems());
        System.out.println(itemDao.SearchItems("奶龙唐唐表情包"));
    }

    @Test
    void test4() {
        List<Category> categoryList = catalogService.getCategories();
        List<Product> productList = productService.getProducts();
        System.out.println(categoryList);
        System.out.println(productList);
    }

    @Test
    void test5() {
        System.out.println(cartService.getCart(12));
    }
}
