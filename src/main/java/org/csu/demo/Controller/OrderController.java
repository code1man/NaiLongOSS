package org.csu.demo.Controller;

import jakarta.servlet.http.HttpSession;
import org.csu.demo.domain.Address;
import org.csu.demo.domain.Cart;
import org.csu.demo.domain.Item;
import org.csu.demo.domain.User;
import org.csu.demo.persistence.AddressDao;
import org.csu.demo.persistence.UserDao;
import org.csu.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes({"loginUser","addressList","item","AddressMsg","cart","totalAmount"})
public class OrderController {


    private final AddressDao addressDao;


    @Autowired
    public OrderController(AddressDao addressDao, UserService userService, UserDao userDao) {
        this.addressDao = addressDao;
    }

    // 从session中获取item，加入model(前端th访问的是model中的属性！)
    @ModelAttribute("item")
    public Item getItem(HttpSession session) {
        return (Item) session.getAttribute("item");
    }

    @ModelAttribute("cart")
    public Cart getCart(HttpSession session) {
        return (Cart) session.getAttribute("cart");
    }

    @ModelAttribute("totalAmount")
    public BigDecimal getTotalAmount(HttpSession session) {
        return (BigDecimal) session.getAttribute("totalAmount");
    }

    //进入订单结算界面
    @GetMapping("/orderForm")
    public String orderForm(@ModelAttribute("loginUser")User user, Model model) {
        if (user == null) {
            // 用户未登录，设置一个标志用于前端提示
            model.addAttribute("notLoggedIn", true);
        } else {
            // 用户已登录，从数据库获取用户地址
            int userId = user.getId();
            List<Address> addressList = addressDao.getAllAddressById(userId);
            // 将地址列表保存到session中
            model.addAttribute("addressList", addressList);
//            System.out.println("返回给前端的 addressList："+addressList);
        }
        return "Order";
    }


    // 进入购物车结算界面
    @GetMapping("/CartCount")
    public String CartCount(@ModelAttribute("loginUser")User user, Model model) {
        if (user == null) {
            // 用户未登录，设置一个标志用于前端提示
            model.addAttribute("notLoggedIn", true);
        } else {
            // 用户已登录，从数据库获取用户地址
            int userId = user.getId();
            List<Address> addressList = addressDao.getAllAddressById(userId);
            // 将地址列表保存到session中
            model.addAttribute("addressList", addressList);
        }
        return "CartToOrder";
    }

    // 处理提交购物车订单，清楚购物车数据
    @PostMapping("/CartHandler")
    public String CartHandler(@ModelAttribute("loginUser")User user, Model model) {
       //
         // 清空购物车逻辑
        return "redirect:/mainForm";

    }

    //处理新增地址需求
    @PostMapping("/order")
    public String order(@RequestBody Address newAddress, @ModelAttribute("loginUser")User user, Model model) {
        if(user == null)
        {
            model.addAttribute("AddressMsg","{\"status\":\"error\", \"message\":\"User not logged in\"}");
            return "redirect:/orderForm";
        }
        int isDefault = newAddress.getIsDefault();
        if(isDefault!=0) addressDao.updateDefaultAddress(user.getId());
        newAddress.setUserId(user.getId());
        addressDao.addAddress(newAddress);
        model.addAttribute("AddressMsg","{\"status\":\"success\"}");
        return "redirect:/orderForm";
    }

    // 处理删除地址的需求
    // 对应前端需要的是一个JSON的响应，若返回界面会继续去请求
    @DeleteMapping("/order/{addressId}")
    @ResponseBody
    public Map<String, String> delete(@PathVariable int addressId) {
        addressDao.deleteAddress(addressId);
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return response;
    }

}

