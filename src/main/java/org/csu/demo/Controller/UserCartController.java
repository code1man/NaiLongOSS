package org.csu.demo.Controller;

import com.google.gson.Gson;
import org.csu.demo.domain.Cart;
import org.csu.demo.domain.Item;
import org.csu.demo.domain.User;
import org.csu.demo.service.CartService;
import org.csu.demo.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@SessionAttributes("cart")
public class UserCartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private ItemService itemService;

    @PostMapping("updateCart")
    public ResponseEntity<String> updateCart(
            @SessionAttribute(value = "loginUser", required = false) User user,
            @SessionAttribute(value = "cart", required = false) Cart cart,
            @RequestBody Map<String, String> requestData,
            Model model) {
        // 确保 cart 不为空
        if (cart == null) {
            cart = new Cart();
        }
        try {
            int userId = user.getId();
            int itemId = Integer.parseInt(requestData.getOrDefault("itemID", "-1"));
            int count = Integer.parseInt(requestData.getOrDefault("count", "0"));
            if (itemId < 0 || count < 0) {
                return ResponseEntity.badRequest().body("{\"error\": \"无效的 itemID 或 count\"}");
            }
            // 更新购物车
            cart = cartService.updateCart(userId, itemId, count, cart);
            if (cart == null) {
                return ResponseEntity.status(500).body("{\"error\": \"购物车更新失败\"}");
            }
            // 更新 session
            cart = cartService.updateCart(userId, itemId, count, cart);
            model.addAttribute("cart", cart);

            return ResponseEntity.ok("商品已更新");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("{\"error\": \"请求参数错误\"}");
        }
    }

    @GetMapping("/AddItemToCart")
    public ResponseEntity<String> addItemToCart(@RequestParam int itemId,
                                                @SessionAttribute("cart") Cart cart,
                                                @SessionAttribute(name = "loginUser", required = false) User user, Model model) {
        // 获取商品
        Item item = itemService.getTtemByItemId(itemId);
        if (item == null) {
            return ResponseEntity.badRequest().body("商品信息错误或缺失");
        }
        if (cart == null) {
            cart = Cart.builder().userId(user.getId()).build();
        }

        // 检查商品是否已在购物车中
        if (cartService.containsItemId(cart, itemId)) {
            cart = cartService.incrementQuantityByItemId(cart, itemId);
        } else {
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请先登录");
            }
            cart = cartService.addItemToCart(cart, item);
        }
        model.addAttribute("cart", cart);

        return ResponseEntity.ok("商品已添加到购物车");
    }

    @PostMapping("removeItem")
    public ResponseEntity<String> removeItem(@RequestBody Map<String, Integer> map,
                                             @SessionAttribute(name = "cart", required = false) Cart cart,
                                             @SessionAttribute(name = "loginUser", required = false) User user,
                                             Model model) {
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("购物车未初始化");
        }

        int itemId = map.get("itemId"); // 解析 JSON 数据

        if (cartService.containsItemId(cart, itemId)) {
            cart = cartService.incrementQuantityByItemId(cart, itemId);
        } else {
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请先登录");
            }
            cart = cartService.removeItemFromCart(cart, itemId);
        }

        model.addAttribute("cart", cart);
        return ResponseEntity.ok("商品已从购物车移除");
    }

}
