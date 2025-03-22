package org.csu.demo.Controller;

import org.csu.demo.domain.Order;
import org.csu.demo.domain.OrderItem;
import org.csu.demo.domain.User;
import org.csu.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/business")
@SessionAttributes({"loginUser"})
public class businessOrderController {

    @Autowired
    private OrderService orderService;
    @GetMapping("/myOrder")
    public String myOrder(@ModelAttribute("loginUser") User user, Model model){
        List<Order> orderList = new ArrayList<Order>();
        List<OrderItem> orderItems = orderService.getOrderItems(user.getId(),orderList,1);
        model.addAttribute("orderItems",orderItems);
        System.out.println(orderItems);
        return "MyBusinessOrder";
    }
}
