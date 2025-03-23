package org.csu.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.demo.domain.*;
import org.csu.demo.persistence.BusinessDao;
import lombok.extern.log4j.Log4j2;
import org.csu.demo.persistence.mappers.OrderMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service("orderService")
@Log4j2
@MapperScan("org.csu.demo.persistence.mappers")
@MapperScan("org.csu.demo.persistence")
@SessionAttributes({"currentOrderList","currentOrder"})
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private BusinessDao businessDao;
    @Autowired
    private ItemService itemService;
    @Autowired
    private BusinessService BusinessService;

    //获取带格式的时间
    public static String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); // 时间格式
        String timePart = sdf.format(new Date()); // 获取当前时间
        return timePart ;
    }

    //生成五位随机数
    public static int getRandomNum(){
        Random random = new Random();
        int randomPart = 10000 + random.nextInt(90000); // 生成5位随机数
        return randomPart;
    }

    //生成订单编号
    public static String generateOrderNumber() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); // 时间格式
        String timePart = sdf.format(new Date()); // 获取当前时间

        Random random = new Random();
        int randomPart = 10000 + random.nextInt(90000); // 生成5位随机数

        return timePart + randomPart;
    }

    //在提交订单后生成(CartItem)
    public boolean addNewOrder(User user, String address, List<CartItem> cartItems, Model model){
        String time = getTime();
        Date date = new Date();
        List<Order> orderList = new ArrayList<>(); // 用于存储本次创建的订单
        for(CartItem cartItem : cartItems){
            //判断库存还有吗？
//            int remain = BusinessService.getItemCount(cartItem.getItemID());
//            if(remain >= 1){
                //保证一次购买多种商品订单编号不一致
                String orderID = time + getRandomNum();
                int supplier = businessDao.getSupplierByItemId(cartItem.getItemID());
                Order order = new Order(orderID, user.getId(), Integer.parseInt(address), cartItem.getItemID(), cartItem.getItemNum(), cartItem.getPrice()*cartItem.getItemNum(), supplier,
                        0, date, null, null, null, null, null, "");
                orderMapper.insert(order);
                orderList.add(order);
            }
//        }
        model.addAttribute("currentOrderList",orderList);
//        System.out.println(orderList);
        return true;
    }

    //在提交订单后生成(item),目前实际上items里面只有一个
    public boolean addNewOrder2(User user, String address, List<Item> items, Model model){
        String time = getTime();
        Date date = new Date();
        for(Item item : items){
            //保证一次购买多种商品订单编号不一致
            String orderID = time + getRandomNum();
            int supplier = businessDao.getSupplierByItemId(item.getId());
            Order order = new Order(orderID, user.getId(), Integer.parseInt(address),item.getId(), 1, item.getPrice(), supplier,
                    0, date, null, null, null, null, null, "");
            orderMapper.insert(order);
            model.addAttribute("currentOrder", order);
        }
        return true;
    }

    //要改成按照时间
    //根据买家id查询订单
    //identify(0买家  1商家)
    public List<Order> getOrderListByClient(int id,int identify){
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if(identify == 0)
            queryWrapper.eq("client",id).orderByDesc("create_time");
        else
            queryWrapper.eq("supplier",id).orderByDesc("create_time");
        List<Order> orders = orderMapper.selectList(queryWrapper);
        return orders;
    }

    //更新订单(通过改bean)
    public void updateOrder(Order order, String status){
        order.setStatus(Integer.parseInt(status));
        switch (status){
            case "1": {
                order.setPay_time(new Date());
            }break;
            case "2": break;
        }
        orderMapper.updateById(order);
    }

    //更新orderItems(用于呈现在myOrder中)
    //identify(0买家  1商家)
    public List<OrderItem> getOrderItems(int userid, List<Order> orderList, int identify){
        orderList = getOrderListByClient(userid,identify);
        List<OrderItem> orderItems = new ArrayList<>();
        for(Order order : orderList){
            Item item = itemService.getItemByItemId(order.getItem_id());
            OrderItem orderItem = new OrderItem(order.getOrder_id(),item.getId(), order.getAmount(), item.getName(), item.getUrl(), item.getPrice(), order.getClient(), order.getStatus());
            orderItems.add(orderItem);
        }
        return orderItems;
    }

}
