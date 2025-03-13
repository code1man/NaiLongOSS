package org.csu.demo.Controller;

import com.google.gson.Gson;
import org.csu.demo.domain.Item;
import org.csu.demo.domain.User;
import org.csu.demo.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@Validated
@SessionAttributes("item")
public class MainController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/mainForm")
    public String loginForm() {
        return "main";
    }

    @GetMapping("/ShoppingCart")
    public String addItemToCart(@RequestParam int itemId, Model model) {
        // 通过数据库找到对应商品
        Item item = itemService.getTtemByItemId(itemId);
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
