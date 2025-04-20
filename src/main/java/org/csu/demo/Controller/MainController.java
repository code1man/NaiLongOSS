package org.csu.demo.Controller;

import com.google.gson.Gson;
import org.csu.demo.common.CommonResponse;
import org.csu.demo.domain.Category;
import org.csu.demo.domain.Item;
import org.csu.demo.domain.OrderItem;
import org.csu.demo.domain.Product;
import org.csu.demo.service.CatalogService;
import org.csu.demo.service.ItemService;
import org.csu.demo.service.OrderService;
import org.csu.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Validated
@RestController
@SessionAttributes("item")
public class MainController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/homepage-data")
    public CommonResponse<Map<String, Object>> getHomepageData() {
        try {
            List<Category> categoryList = catalogService.getCategories();
            List<Product> productList = productService.getProducts();
            List<OrderItem> orderList = orderService.getAllOrders();

            Map<String, Object> data = new HashMap<>();
            data.put("categoryList", categoryList);
            data.put("productList", productList);
            data.put("orderList", orderList);

            return CommonResponse.createForSuccess(data);
        } catch (Exception e) {
            return CommonResponse.createForError("获取首页数据失败：" + e.getMessage());
        }
    }

    @GetMapping("/ShoppingCart")
    public String addItemToCart(@RequestParam int itemId, Model model) {
        // 通过数据库找到对应商品
        Item item = itemService.getItemByItemId(itemId);
        System.out.println(item);
        if (item == null) {
            return "redirect:/mainForm";
        }
        model.addAttribute("item", item);

        return "ShoppingCart";
    }

    @PostMapping("/search")
    public ResponseEntity<String> searchItemByKey(@RequestBody Map<String, String> requestData) {
        String key = requestData.get("key");

        if (key != null && !key.isEmpty()) {
            List<Item> searchResults = itemService.SearchItems(key);  // 调用服务层方法查询数据库
            String json = new Gson().toJson(searchResults);  // 使用 Gson 将结果转换为 JSON 字符串
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(json);
        }

        return ResponseEntity.badRequest().body("{\"error\": \"搜索关键词不能为空\"}");
    }
}
