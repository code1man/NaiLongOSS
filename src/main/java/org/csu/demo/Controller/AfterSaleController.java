package org.csu.demo.Controller;


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
    @Autowired
    private AfterSaleMapper afterSaleMapper;


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

        afterSaleMapper.updateById(afterSale);
        return "success";
    }
}
