package org.csu.demo.Controller;

import com.google.gson.Gson;
import org.csu.demo.common.CommonResponse;
import org.csu.demo.common.ResponseCode;
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
@RestController
@Controller
public class UserCartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private ItemService itemService;

    /*
    * 偷懒，增加减少都用这个
    */
    // 方式二：传 itemId 和 count，设置数量
    @PostMapping("/updateCart/{itemId}/{count}")
    @ResponseBody
    public CommonResponse<String> updateCartWithCount(@PathVariable int itemId,
                                                      @PathVariable int count,
                                                      @RequestParam int userId) {
        if (itemId < 0 || count < 0) {
            return CommonResponse.createForError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数非法");
        }
        cartService.updateCart(userId, itemId, count);
        return CommonResponse.createForSuccess("商品数量已更新");
    }

    @GetMapping("/cart/{itemId}")
    public CommonResponse<String> addItemToCart(@PathVariable int itemId,
                                                @RequestParam int userId) {

        // 检查商品是否已在购物车中
        if (cartService.containsItemId(userId, itemId)) {
            cartService.incrementQuantityByItemId(userId, itemId);
        } else {
            cartService.addItemToCart(userId, itemId);
        }

        return CommonResponse.createForSuccess("商品已添加至购物车");
    }

    @DeleteMapping("/removeItem/{itemId}")
    @ResponseBody
    public CommonResponse<String> removeItem(@PathVariable int itemId,
                                             @RequestParam int userId) {

        cartService.removeItemFromCart(userId, itemId);
        return CommonResponse.createForSuccess("商品已从购物车移除");
    }

    @GetMapping("cart")
    @ResponseBody
    public CommonResponse<Cart> getCart(@RequestParam int userId) {
        Cart cart = cartService.getCart(userId);
        if (cart != null) {
            return CommonResponse.createForSuccess(cart);
        }
        else {
            return CommonResponse.createForError("获取用户购物车失败");
        }
    }

}
