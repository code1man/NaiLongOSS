package org.csu.demo.Controller;

import com.alibaba.fastjson2.JSON;
import org.csu.demo.domain.Order;
import org.csu.demo.domain.OrderItem;
import org.csu.demo.domain.User;
import org.csu.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@SessionAttributes({"orderList"})
public class AdminOrderController {


    @Autowired
    private OrderService orderService;
    @GetMapping("/Control")
    public String myOrder(Model model) {
        // 获取所有订单项
        List<OrderItem> orderItems = orderService.getAllOrders();

        // 将订单项添加到模型中
        model.addAttribute("orderItems", orderItems);

        // 返回页面
        return "AdminOrder";  // 返回 AdminOrder.html 页面
    }


    // 新增一个查询方法，根据用户名查询订单
    @GetMapping("/searchOrder")
    @ResponseBody
    public String searchUserOrder(@RequestParam("orderId")  String orderId, Model model) {
        // 调用Service层根据用户ID查询订单
        List<OrderItem> orderItems= orderService.getOrderItemsByClient(orderId,0);

        model.addAttribute("orderItems",orderItems);

        // 使用 FastJSON 将 orderItems 转换为 JSON 字符串
        String orderItemsJson = JSON.toJSONString(orderItems);
        return orderItemsJson;  // 返回 JSON 字符串
    }


    // 查询超时未处理售后的订单
    @GetMapping("/getTimeoutOrders")
    @ResponseBody
    public String getTimeoutOrders(Model model) {
        // 调用 Service 层的方法，获取超时未处理售后的订单项（OrderItem）
        List<OrderItem> timeoutOrderItems = orderService.getTimeoutOrderItems();

        // 将订单项列表转换为 JSON 字符串
        String timeoutOrdersJson = JSON.toJSONString(timeoutOrderItems);
        return timeoutOrdersJson;  // 返回 JSON 字符串
    }


}
