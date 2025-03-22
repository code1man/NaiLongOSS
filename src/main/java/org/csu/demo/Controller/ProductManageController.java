package org.csu.demo.Controller;

import org.csu.demo.domain.Item;
import org.csu.demo.service.BusinessService;
import org.csu.demo.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class ProductManageController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private BusinessService businessService;
    @GetMapping("/ProductManage")
    public String productsPage() {
        return "ProductManage";
    }

    // 数据接口（处理前端表格请求）
    @GetMapping("/api/products")
    @ResponseBody
     public List<Item> getAllProducts( @RequestParam(name = "categoryId", required = false) Integer categoryId) {
        // 添加调试日志
        System.out.println("getAllProducts");
        System.out.println("[DEBUG] 请求到达 /api/products，categoryId=" + categoryId);

        return businessService.getBusinessItemById(categoryId);
    }


    @GetMapping("/api/products/{productId}")
    @ResponseBody
    public Item getProductById(@PathVariable("productId") int productId) { // 明确指定路径变量名

        System.out.println("✅ 收到商品详情请求，ID: " + productId);
        System.out.println(itemService.getItemByItemId(productId));

        return itemService.getItemByItemId(productId) ;
    }


}



