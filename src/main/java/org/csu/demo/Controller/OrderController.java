package org.csu.demo.Controller;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpSession;
import org.csu.demo.common.CommonResponse;
import org.csu.demo.domain.*;
import org.csu.demo.domain.DTO.ItemSubmitObject;
import org.csu.demo.domain.DTO.OrderStatusChangeRequest1;
import org.csu.demo.domain.DTO.OrderStatusChangeRequest2;
import org.csu.demo.domain.VO.CartOrder;
import org.csu.demo.persistence.AddressDao;
import org.csu.demo.persistence.UserDao;
import org.csu.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.events.Comment;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
//@CrossOrigin  //暂时解决跨域问题

@SessionAttributes({"loginUser","addressList","item","AddressMsg","cart","totalAmount","orderList","currentOrderList","currentOrder"})
public class OrderController {

    private final AddressDao addressDao;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private BusinessService BusinessService;

    @Autowired
    private CartService cartService;

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

    //上一个方法修改（用于前后端分离）
    @GetMapping("/singleOrder")
    @ResponseBody
    public CommonResponse<List<Address>> orderForm1(@RequestParam("userId") int userId) {
            List<Address> addressList = addressDao.getAllAddressById(userId);
            // 将地址列表保存到session中
            return CommonResponse.createForSuccess(addressList);
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

    // 进入购物车结算界面的修改（用于前后端分离）
    @GetMapping("/cartOrder")
    @ResponseBody
    public CommonResponse<CartOrder> CartCount1(@RequestParam("userId")int userId) {

        Cart cart = cartService.getCart(userId);
        List<Address> addressList = addressDao.getAllAddressById(userId);

        CartOrder cartOrder = new CartOrder();
        cartOrder.setAddressList(addressList);
        cartOrder.setCartItemList(cart.getItemList());

        return CommonResponse.createForSuccess(cartOrder);
    }

    // 处理提交购物车订单，清楚购物车数据
    @PostMapping("/CartHandler")
    @ResponseBody
    public CommonResponse<List<Order>> CartHandler(HttpSession session,@RequestParam("address") String addressID, @ModelAttribute("loginUser")User user,@ModelAttribute("cart")Cart cart, Model model) {
       //
         // 清空购物车逻辑
        session.removeAttribute("cart");
        //增加新订单

        List<CartItem> cartItems = cart.getItemList();
        List<Order> currentOrderList = orderService.addNewOrder1(user, addressID, cartItems);
        //这里要返回 currentOrderList,便于前端回传修改状态
        return CommonResponse.createForSuccess(currentOrderList);
    }

    // 处理提交购物车订单，清楚购物车数据（用于前后端分离）
    @PostMapping("/CartSubmit")
    @ResponseBody
    public CommonResponse<List<Order>> CartHandler1(@RequestParam("userId") int userId,@RequestParam("address") String addressID) {
        //
        // 清空购物车逻辑
        System.out.println("/CartSubmit : here.....");
        //增加新订单
        Cart cart = cartService.getCart(userId);
        List<CartItem> cartItems = cart.getItemList();
        System.out.println("/CartSubmit cartItems: " + cartItems);

        User user = userService.getUser(userId);

        List<Order> currentOrderList = orderService.addNewOrder1(user, addressID, cartItems);
        System.out.println("/CartSubmit currentOrderList: " + currentOrderList);
        //这里要返回 currentOrderList,便于前端回传修改状态
        return CommonResponse.createForSuccess(currentOrderList);
    }


    // 处理提交购物车订单，清楚购物车数据
    @PostMapping("/ItemHandler")
    @ResponseBody
    public CommonResponse<Order> ItemHandler(@RequestParam("address") String addressID, @ModelAttribute("loginUser")User user,@ModelAttribute("item")Item item, Model model) {
        //增加新订单
        List<Item> items = new ArrayList<>();
        items.add(item);


        //这里要返回 currentOrder,便于前端回传修改状态
        Order order = orderService.addNewOrder3(user, addressID, items);
        if(order == null){
            return CommonResponse.createForError("库存不足");
        }
        return CommonResponse.createForSuccess(order);
    }


    // 处理提交购物车订单，清楚购物车数据(用于前后端分离)
    @PostMapping("/ItemSubmit")
    @ResponseBody
    public CommonResponse<Order> ItemHandler1(@RequestBody ItemSubmitObject itemSubmitObject) {
        //增加新订单
        List<Item> items = new ArrayList<>();
        items.add(itemSubmitObject.getItem());
        System.out.println(itemSubmitObject.getItem());
        System.out.println(itemSubmitObject.getAddressID());

        User user = userService.getUser(itemSubmitObject.getUserId());
        //这里要返回 currentOrder,便于前端回传修改状态
        System.out.println("111111.....");
        Order order = orderService.addNewOrder3(user, itemSubmitObject.getAddressID(), items);
        System.out.println("22222222.....");
        if(order == null){
            return CommonResponse.createForError("库存不足");
        }
        return CommonResponse.createForSuccess(order);
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

    /*
    //点击查看我的订单
    @GetMapping("/myOrder")
    public String myOrder(@ModelAttribute("orderList")List<Order> orderList, @ModelAttribute("loginUser")User user, Model model){
        System.out.println("orderList"+orderList);
        List<OrderItem> orderItems = orderService.getOrderItems(user.getId(),orderList,0);
        model.addAttribute("orderItems",orderItems);
        System.out.println(orderItems);
        return "MyOrder";
    }
*/

    //点击查看我的订单（api重新设计）
    @GetMapping("/orders/{userId}")
    @ResponseBody
    public CommonResponse<List<OrderItem>> myOrder(@PathVariable("userId") int userId){

        //用户名不存在
        if(userService.getUser(userId) == null){
            return CommonResponse.createForError("服务器内部异常");
        }

        List<Order> orderList = orderService.getOrderListByClient(userId, 0);
        System.out.println("/orders: orderList"+orderList);
        List<OrderItem> orderItems = orderService.getOrderItems(userId,orderList,0);
        System.out.println("/orders: orderList" + orderItems);


        return CommonResponse.createForSuccess(orderItems);
    }

    /*//以下是状态转换，感觉可以统一起来
    //处理整个购物车订单状态转换
    @PostMapping("/statusChangeTo1")
    @ResponseBody
    public String statusChangeTo1(@RequestParam("behavior") String behavior, @RequestParam("nextStatus") String nextStatus, Model model){
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
    }*/

    //经过修改
    //以下是状态转换，感觉可以统一起来
    //处理整个购物车订单状态转换
    @PutMapping ("/orderStatus")
    @ResponseBody
    public CommonResponse<String> statusChangeTo1(@RequestBody OrderStatusChangeRequest1 request){

        Cart cart = cartService.getCart(request.getUserId());
        //从请求中取值
        String behavior = request.getBehavior();
        String nextStatus = request.getNextStatus();
        List<String> currentOrderList = request.getCurrentOrderList();

        System.out.println("orderStatus: " + currentOrderList);

        //整个购物车订单一起购买/单个商品购买
        for(String orderId : currentOrderList){
            Order order = orderService.getOrderByOrderId(orderId);
            cartService.removeItemFromCart(cart,order.getItem_id());
            orderService.updateOrder(orderService.getOrderByOrderId(orderId),nextStatus);
        }
        return CommonResponse.createForSuccessMessage(behavior + " SUCCESS");
    }

    /*@PostMapping("/statusChange")
    @ResponseBody
    public String statusChange(@RequestParam("orderId") String orderId, @RequestParam("nextStatus") String nextStatus, Model model){

        Order order = orderService.getOrderByOrderId(orderId);
        //如果是主动取消订单，也需要返回库存
        if(nextStatus.equals("10")){
            Item item = itemService.getItemByItemId(order.getItem_id());

            int remain = BusinessService.getItemCount(item.getId());
            item.setRemainingNumb(remain+order.getAmount());
            order.setIs_occupy(0);
            BusinessService.updateItem(item);
        }
        //这里连同是否占用库存一起更新
        orderService.updateOrder(order,nextStatus);
        return "success";
    }*/

    //修改后
    @PutMapping ("/status")
    @ResponseBody
    public CommonResponse<String> statusChange(@RequestBody OrderStatusChangeRequest2 request){

        //从请求中取值
        String orderId = request.getOrderId();
        String nextStatus = request.getNextStatus();

        Order order = orderService.getOrderByOrderId(orderId);
        //如果是主动取消订单，也需要返回库存
        if(nextStatus.equals("10")){
            Item item = itemService.getItemByItemId(order.getItem_id());

            int remain = BusinessService.getItemCount(item.getId());
            item.setRemainingNumb(remain+order.getAmount());
            order.setIs_occupy(0);
            BusinessService.updateItem(item);
        }
        //这里连同是否占用库存一起更新
        orderService.updateOrder(order,nextStatus);

        return CommonResponse.createForSuccess();
    }

    /*//买家个人订单刷新
    @GetMapping("/updateMyOrder")
    @ResponseBody
    public String updateMyOrder(@ModelAttribute("orderList")List<Order> orderList, @ModelAttribute("loginUser")User user, Model model){

        List<OrderItem> orderItems = orderService.getOrderItems(user.getId(),orderList,0);
        model.addAttribute("orderItems",orderItems);

        // 使用 FastJSON 将 orderItems 转换为 JSON 字符串
        String orderItemsJson = JSON.toJSONString(orderItems);
        return orderItemsJson;  // 返回 JSON 字符串
    }*/

    //买家个人订单刷新
    @GetMapping("/newOrders/{userId}")
    @ResponseBody
    public CommonResponse<List<OrderItem>> updateMyOrder(@PathVariable("userId") int userId){

        List<Order> orderList = orderService.getOrderListByClient(userId, 0);
        System.out.println("/newOrders: orderList"+orderList);
        List<OrderItem> orderItems = orderService.getOrderItems(userId,orderList,0);
        System.out.println("/newOrders: orderList" + orderItems);

        return CommonResponse.createForSuccess(orderItems);
    }

    @GetMapping("/getAddress")
    @ResponseBody
    public String getAddress1( @RequestParam("orderId") String orderId){
        int addressId = orderService.getOrderByOrderId(orderId).getAddress_id();
        Address address = addressDao.getAddressById(addressId);
        String json = JSON.toJSONString(address);
        return json;  // 返回 JSON 字符串
    }

    //修改后
    @GetMapping("/address/{orderId}")
    @ResponseBody
    public CommonResponse<Address> getAddress( @PathVariable("orderId") String orderId){
        int addressId = orderService.getOrderByOrderId(orderId).getAddress_id();
        Address address = addressDao.getAddressById(addressId);
        return CommonResponse.createForSuccess(address);
    }

    @GetMapping("/getOrder")
    @ResponseBody
    public String getOrder1(@RequestParam("orderId") String orderId){
        Order order = orderService.getOrderByOrderId(orderId);
        return JSON.toJSONString(order);
    }

    //修改后
    @GetMapping("/{orderId}")
    @ResponseBody
    public CommonResponse<Order> getOrder(@PathVariable("orderId") String orderId){
        Order order = orderService.getOrderByOrderId(orderId);
        return CommonResponse.createForSuccess(order);
    }

    //从个人订单列表中点击支付按钮
//    @GetMapping("/orderForm1")
//    public String orderForm1(@RequestParam("flag") String flag, @RequestParam("orderId") String orderId, Model model){
//        if(flag.equals("fromMyOrder")){
//            Order order = orderService.getOrderByOrderId(orderId);
//            Item item = itemService.getTtemByItemId(order.getItem_id());
//            model.addAttribute("item",item);
//        }
//    }
}

