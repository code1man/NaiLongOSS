package org.csu.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.csu.demo.domain.*;
import org.csu.demo.persistence.BusinessDao;
import org.csu.demo.persistence.mappers.AfterSaleMapper;
import lombok.extern.log4j.Log4j2;
import org.csu.demo.persistence.UserDao;
import org.csu.demo.persistence.mappers.OrderMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service("orderService")
@MapperScan("org.csu.demo.persistence")
@SessionAttributes({"currentOrderList","currentOrder"})
public class OrderService extends ServiceImpl<OrderMapper, Order> {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private BusinessDao businessDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ItemService itemService;
    @Autowired
    private BusinessService BusinessService;
    @Autowired
    private AfterSaleMapper afterSaleMapper;
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

    public void updateOrder(Order order){
        orderMapper.updateById(order);
    }

    //在提交订单后生成(CartItem)
    public boolean addNewOrder(User user, String address, List<CartItem> cartItems, Model model){
        String time = getTime();
        Date date = new Date();
        List<Order> orderList = new ArrayList<>(); // 用于存储本次创建的订单
        for(CartItem cartItem : cartItems){
//            判断库存还有吗？
            int remain = BusinessService.getItemCount(cartItem.getItemID());
            if(remain >= cartItem.getItemNum()){
                //减少库存
                 Item item = itemService.getItemByItemId(cartItem.getItemID());
                item.setRemainingNumb(remain-cartItem.getItemNum());
                BusinessService.updateItem(item);

                //保证一次购买多种商品订单编号不一致
                String orderID = time + getRandomNum();
                int supplier = businessDao.getSupplierByItemId(cartItem.getItemID());
                Order order = new Order(orderID, user.getId(), Integer.parseInt(address), cartItem.getItemID(), cartItem.getItemNum(), cartItem.getPrice()*cartItem.getItemNum(), supplier,
                        0, date, null, null, null, null, null, "",1);
                orderMapper.insert(order);
                orderList.add(order);
                AfterSale afterSale = new AfterSale(orderID, 0,0,0,null,null);
                afterSaleMapper.insert(afterSale);
            }
        }
        model.addAttribute("currentOrderList",orderList);
//        System.out.println(orderList);
        return true;
    }

    public List<Order> addNewOrder1(User user, String address, List<CartItem> cartItems){
        String time = getTime();
        Date date = new Date();
        List<Order> orderList = new ArrayList<>(); // 用于存储本次创建的订单
        for(CartItem cartItem : cartItems){
//            判断库存还有吗？
            int remain = BusinessService.getItemCount(cartItem.getItemID());
            if(remain >= cartItem.getItemNum()){
                //减少库存
                Item item = itemService.getItemByItemId(cartItem.getItemID());
                item.setRemainingNumb(remain-cartItem.getItemNum());
                BusinessService.updateItem(item);

                //保证一次购买多种商品订单编号不一致
                String orderID = time + getRandomNum();
                int supplier = businessDao.getSupplierByItemId(cartItem.getItemID());
                Order order = new Order(orderID, user.getId(), Integer.parseInt(address), cartItem.getItemID(), cartItem.getItemNum(), cartItem.getPrice()*cartItem.getItemNum(), supplier,
                        0, date, null, null, null, null, null, "",1);
                orderMapper.insert(order);
                orderList.add(order);
                AfterSale afterSale = new AfterSale(orderID, 0,0,0,null,null);
                afterSaleMapper.insert(afterSale);
            }
        }
//        System.out.println(orderList);
        return orderList;
    }



    //在提交订单后生成(item),目前实际上items里面只有一个
    public boolean addNewOrder2(User user, String address, List<Item> items, Model model){
        String time = getTime();
        Date date = new Date();
        for(Item item : items){
            //因为之前取值的时候item里面的最后一个属性是没取到的（所以这里干脆直接查）
            int remain = BusinessService.getItemCount(item.getId());
            if(remain >= 1) {
                //减少库存
                item.setRemainingNumb(remain - 1);
                BusinessService.updateItem(item);

                //保证一次购买多种商品订单编号不一致
                String orderID = time + getRandomNum();
                int supplier = businessDao.getSupplierByItemId(item.getId());
                Order order = new Order(orderID, user.getId(), Integer.parseInt(address), item.getId(), 1, item.getPrice(), supplier,
                        0, date, null, null, null, null, null, "", 1);
                AfterSale afterSale = new AfterSale(orderID, 0,0,0,null,null);
                afterSaleMapper.insert(afterSale);
                orderMapper.insert(order);
                model.addAttribute("currentOrder", order);
                return true;
            }
        }
        return false;
    }

    //在提交订单后生成(item),目前实际上items里面只有一个
    public Order addNewOrder3(User user, String address, List<Item> items){
        String time = getTime();
        Date date = new Date();
        for(Item item : items){
            //因为之前取值的时候item里面的最后一个属性是没取到的（所以这里干脆直接查）
            int remain = BusinessService.getItemCount(item.getId());
            if(remain >= 1) {
                //减少库存
                item.setRemainingNumb(remain - 1);
                BusinessService.updateItem(item);

                //保证一次购买多种商品订单编号不一致
                String orderID = time + getRandomNum();
                int supplier = businessDao.getSupplierByItemId(item.getId());
                Order order = new Order(orderID, user.getId(), Integer.parseInt(address), item.getId(), 1, item.getPrice(), supplier,
                        0, date, null, null, null, null, null, "", 1);
                AfterSale afterSale = new AfterSale(orderID, 0,0,0,null,null);
                afterSaleMapper.insert(afterSale);
                orderMapper.insert(order);
                return order;
            }
        }
        //库存不足
        return null;
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
            case "2": {
                order.setShip_time(new Date());
            };break;
            case "4":{
                order.setConfirm_time(new Date());
            }break;
            case "11":{
                order.setAfter_sale_time(new Date());
            }break;
        }
        orderMapper.updateById(order);
    }

    //更新orderItems(用于呈现在myOrder中)
    //identify(0买家  1商家)
    public List<OrderItem> getOrderItems(int userid, List<Order> orderList, int identify){
        orderList = getOrderListByClient(userid,identify);
        List<OrderItem> orderItems = new ArrayList<>();
        for(Order order : orderList){
            if(order.getStatus() == 0 && isOverTime(order.getCreate_time(),15)){
                continue;
            }
            Item item = itemService.getItemByItemId(order.getItem_id());

            OrderItem orderItem = new OrderItem(order.getOrder_id(),item.getId(), order.getAmount(), item.getName(), item.getUrl(), item.getPrice(), order.getClient(), order.getStatus());
            orderItems.add(orderItem);
        }
        return orderItems;
    }
    public List<OrderItem> getOrderItemsByClient(String username, int identify) {

        // 通过用户名查询用户ID
        Integer userid = userDao.getUserIdByUsername(username);
        // 创建一个查询条件，获取指定用户的所有订单列表
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if (identify == 0) {
            queryWrapper.eq("client", userid).orderByDesc("create_time");  // 买家
        } else {
            queryWrapper.eq("supplier", userid).orderByDesc("create_time");  // 商家
        }

        // 执行查询并获取订单列表
        List<Order> orderList = orderMapper.selectList(queryWrapper);

        // 创建一个 List 用于存储订单项
        List<OrderItem> orderItems = new ArrayList<>();

        // 遍历所有的订单
        for (Order order : orderList) {
            // 根据订单中的商品 ID 获取该商品的详细信息
            Item item = itemService.getItemByItemId(order.getItem_id());

            // 创建 OrderItem 对象，将订单与商品信息封装
            OrderItem orderItem = new OrderItem(order.getOrder_id(), item.getId(), order.getAmount(), item.getName(), item.getUrl(), item.getPrice(), order.getClient(), order.getStatus());

            // 将订单项添加到 orderItems 列表中
            orderItems.add(orderItem);
        }

        // 返回所有订单项
        return orderItems;
    }
    // 获取超时没有售后的订单并转换为订单项 (OrderItem)
    public List<OrderItem> getTimeoutOrderItems() {
        // 获取当前时间
        Date currentTime = new Date();

        // 查询所有订单
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        List<Order> allOrders = orderMapper.selectList(queryWrapper);

        // 存储超时的订单项
        List<OrderItem> timeoutOrderItems = new ArrayList<>();

        // 遍历所有订单，判断是否超时
        for (Order order : allOrders) {
            Date afterSaleTime = order.getAfter_sale_time();
            if (afterSaleTime != null) {
                // 判断是否超过5分钟
                long diff = currentTime.getTime() - afterSaleTime.getTime();
                if (diff > 5 * 60 * 1000) { // 超过5分钟，说明是超时订单
                    // 根据订单中的商品 ID 获取该商品的详细信息
                    Item item = itemService.getItemByItemId(order.getItem_id());

                    // 创建 OrderItem 对象，将订单与商品信息封装
                    OrderItem orderItem = new OrderItem(
                            order.getOrder_id(), item.getId(), order.getAmount(),
                            item.getName(), item.getUrl(), item.getPrice(),
                            order.getClient(), order.getStatus()
                    );

                    // 将订单项添加到超时订单项列表中
                    timeoutOrderItems.add(orderItem);
                }
            }
        }

        // 返回所有超时订单项
        return timeoutOrderItems;
    }
    public List<OrderItem> getAllOrders() {
        // 查询所有订单
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        List<Order> allOrders = orderMapper.selectList(queryWrapper);

        // 存储所有的订单项
        List<OrderItem> orderItems = new ArrayList<>();

        // 遍历所有的订单
        for (Order order : allOrders) {
            // 根据订单中的商品 ID 获取该商品的详细信息
            Item item = itemService.getItemByItemId(order.getItem_id());

            // 创建 OrderItem 对象，将订单与商品信息封装
            OrderItem orderItem = new OrderItem(
                    order.getOrder_id(), item.getId(), order.getAmount(),
                    item.getName(), item.getUrl(), item.getPrice(),
                    order.getClient(), order.getStatus()
            );

            // 将订单项添加到 orderItems 列表中
            orderItems.add(orderItem);
        }

        // 返回所有订单项
        return orderItems;
    }

    //判断某个DATE类型的时间距离当前时间是否超过minute分钟
    public boolean isOverTime(Date time, int minute){
        LocalDateTime createTime = time.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        // 获取当前时间
        LocalDateTime currentTime = LocalDateTime.now();

        // 计算时间差
        Duration duration = Duration.between(createTime, currentTime);

        // 如果差距超过分钟，则执行相应操作
        return duration.toMinutes() >= minute;
    }

    //通过orderID获得order
    public Order getOrderByOrderId(String orderId){
        return orderMapper.selectById(orderId);
    }

    public AfterSale getAfterSale(String orderId){
        return afterSaleMapper.selectById(orderId);
    }

    //更新售后订单
    public void updateAfterSale(AfterSale afterSale){
        afterSaleMapper.updateById(afterSale);
    }

    @Scheduled(fixedRate = 60000) // 每 60 秒执行一次,处理超时订单库存返回。。。
    public void checkUnpaidOrders() {
        System.out.println("定时任务执行中..." + System.currentTimeMillis());
// 获取当前时间，并减去 15 分钟
        LocalDateTime fifteenMinutesAgo = LocalDateTime.now().minusMinutes(15);

        // 构建查询条件
        LambdaQueryWrapper<Order> queryWrapper = Wrappers.lambdaQuery(Order.class)
                .eq(Order::getIs_occupy, 1) // is_occupy = 1
                .eq(Order::getStatus, 0)   // status = 0
                .lt(Order::getCreate_time, fifteenMinutesAgo); // create_time < 当前时间 - 15分钟

        // 执行查询
        List<Order> orders = this.list(queryWrapper);
        for(Order order: orders){
            //将是否占用库存改成不占用
            order.setIs_occupy(0);
            orderMapper.updateById(order);

            //更新库存
            Item item = itemService.getItemByItemId(order.getItem_id());
            int remain = BusinessService.getItemCount(order.getItem_id());
            item.setRemainingNumb(remain+order.getAmount());
            BusinessService.updateItem(item);
        }
    }

}
