package org.csu.demo.Controller;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpSession;
import org.csu.demo.domain.*;
import org.csu.demo.persistence.AddressDao;
import org.csu.demo.persistence.UserDao;
import org.csu.demo.service.ItemService;
import org.csu.demo.service.OrderService;
import org.csu.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes({"loginUser","addressList","item","AddressMsg","cart","totalAmount","orderList","currentOrderList","currentOrder"})
public class OrderController {


    private final AddressDao addressDao;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemService itemService;

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

//    @ModelAttribute("totalAmount")                                // 改为直接到session中取了
//    public BigDecimal getTotalAmount(HttpSession session) {
//        return (BigDecimal) session.getAttribute("totalAmount");
//    }

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
            model.addAttribute("totalAmount", 1);
        }
        return "CartToOrder";
    }

    // 处理提交购物车订单，清楚购物车数据
    @PostMapping("/CartHandler")
    @ResponseBody
    public String CartHandler(@RequestParam("address") String addressID, @ModelAttribute("loginUser")User user,@ModelAttribute("cart")Cart cart, Model model) {
       //
         // 清空购物车逻辑

        //增加新订单
        List<CartItem> cartItems = cart.getItemList();
        if(orderService.addNewOrder(user, addressID, cartItems,model)){
            return "success";
        }
       return "fail";
    }

    // 处理提交购物车订单，清楚购物车数据
    @PostMapping("/ItemHandler")
    @ResponseBody
    public String ItemHandler(@RequestParam("address") String addressID, @ModelAttribute("loginUser")User user,@ModelAttribute("item")Item item, Model model) {
        //增加新订单
        List<Item> items = new ArrayList<>();
        items.add(item);
        if(orderService.addNewOrder2(user, addressID, items, model)){
            return "success";
        }
        return "fail";
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

    //点击查看我的订单
    @GetMapping("/myOrder")
    public String myOrder(@ModelAttribute("orderList")List<Order> orderList, @ModelAttribute("loginUser")User user, Model model){

        List<OrderItem> orderItems = orderService.getOrderItems(user.getId(),orderList,0);
        model.addAttribute("orderItems",orderItems);
        System.out.println(orderItems);
        return "MyOrder";
    }

    //处理整个购物车订单状态转换
    @PostMapping("/statusChange")
    @ResponseBody
    public String statusChange(@RequestParam("behavior") String behavior, @RequestParam("nextStatus") String nextStatus, Model model){
        //整个购物车订单一起购买
        if(behavior.equals("payCartOrder")){
            List<Order> currentOrderList = (List<Order>) model.getAttribute("currentOrderList");
            for(Order order : currentOrderList){
                orderService.updateOrder(order,nextStatus);
            }
        }
        //单个商品购买
        else{
            Order currentOrder = (Order) model.getAttribute("currentOrder");
            orderService.updateOrder(currentOrder,nextStatus);
        }

        return "success";
    }

    //买家个人订单刷新
    @GetMapping("/updateMyOrder")
    @ResponseBody
    public String updateMyOrder(@ModelAttribute("orderList")List<Order> orderList, @ModelAttribute("loginUser")User user, Model model){

        List<OrderItem> orderItems = orderService.getOrderItems(user.getId(),orderList,0);
        model.addAttribute("orderItems",orderItems);

        // 使用 FastJSON 将 orderItems 转换为 JSON 字符串
        String orderItemsJson = JSON.toJSONString(orderItems);
        return orderItemsJson;  // 返回 JSON 字符串
    }
}

