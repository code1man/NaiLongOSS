package org.csu.demo;

import org.csu.demo.domain.Address;
import org.csu.demo.domain.Category;
import org.csu.demo.domain.Product;
import org.csu.demo.domain.User;
import org.csu.demo.persistence.*;
import org.csu.demo.service.*;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@MapperScan("org.csu.demo.persistence")
@ActiveProfiles("test")  // 启用 test 这个 Profile
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
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    @Test
    void testAddressDao() {
        List<Address> addressList = addressDao.getAllAddressById(48);
        for(Address address : addressList) {
            System.out.println(address);
        }
    }

    @Test
    void test1(){
        System.out.println(productService.getProductIdByName("奶龙唐唐表情包"));

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
        System.out.println(itemService.getItemsByProductId(2));
    }

    @Test
    void test5() {
        System.out.println(userService.login("奶龙", "nailong"));
        System.out.println(cartService.getCart(12));
    }

    @Test
    void test6(){System.out.println(adminDao.countAllUsers());
        User student = User.builder()
                .id(1)
                .username("张三")
                .password("123456")
                .age(12)
                .email("11@qq.com")
                .build();
        userDao.addUser(student);
    adminDao.freezeUser(1,"297327349237");
        System.out.println(adminDao.countAllUsers());
        System.out.println(adminDao.countFrozenUsers());
        System.out.println(adminDao.getFrozenReason(1));
    }
    @Test
    void test7(){

        adminDao.creditDecrease(2);
        System.out.println(adminDao.getAllMerchants());
    }
}
