package org.csu.demo.Controller;


import com.alibaba.fastjson2.JSON;
import org.aspectj.lang.annotation.After;
import org.csu.demo.domain.AfterSale;
import org.csu.demo.persistence.mappers.AfterSaleMapper;
import org.csu.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping("/afterSale")
public class AfterSaleController {

    @Autowired
    private OrderService orderService;

    //www路径可能得改
    @PostMapping("/buttonClick/{orderId}")
    @ResponseBody
    public String handleButtonClick(@RequestBody Map<String, String> request,
                                    @PathVariable String orderId
    ) {
        System.out.println("接收到的按钮ID: " + request.get("id"));
        //获取按钮id，转换成数字对应状态123www
        int id = parseInt(request.get("id"));

        AfterSale afterSale = orderService.getAfterSale(orderId);

        afterSale.setAfter_sale_status(id);

        orderService.updateAfterSale(afterSale);
        return "success";
    }

    @GetMapping("/getAfterSaleInfo")
    @ResponseBody
    public String statusChange(@RequestParam("orderId") String orderId){
        AfterSale afterSale = orderService.getAfterSale(orderId);
        return JSON.toJSONString(afterSale);
    }
}
