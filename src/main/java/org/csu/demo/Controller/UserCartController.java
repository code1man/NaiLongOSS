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

@Controller
@SessionAttributes("cart")
public class UserCartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private ItemService itemService;

    /*
    * 偷懒，增加减少都用这个
    */
    @PostMapping("updateCart")
    public CommonResponse<String> updateCart(
            @SessionAttribute(value = "loginUser", required = false) User user,
            @SessionAttribute(value = "cart", required = false) Cart cart,
            @RequestBody Map<String, String> requestData) {
        // 确保 cart 不为空
        if (cart == null) {
            cart = new Cart();
        }
        try {
            int userId = user.getId();
            int itemId = Integer.parseInt(requestData.getOrDefault("itemID", "-1"));
            int count = Integer.parseInt(requestData.getOrDefault("count", "0"));
            if (itemId < 0 || count < 0) {
                return CommonResponse.createForError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "商品信息错误");
            }
            // 更新购物车
            cart = cartService.updateCart(userId, itemId, count, cart);
            if (cart == null) {
                return CommonResponse.createForError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "商品信息错误");
            }

            return CommonResponse.createForSuccess("商品已更新");
        } catch (NumberFormatException e) {
            return CommonResponse.createForError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "商品参数错误");
        }
    }

    @GetMapping("/AddItemToCart")
    public CommonResponse<String> addItemToCart(@RequestParam int itemId,
                                                @SessionAttribute("cart") Cart cart,
                                                @SessionAttribute(name = "loginUser", required = false) User user) {
        // 获取商品
        Item item = itemService.getItemByItemId(itemId);
        if (item == null) {
            return CommonResponse.createForError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "商品信息错误");
        }
        if (cart == null) {
            cart = Cart.builder().userId(user.getId()).build();
        }

        // 检查商品是否已在购物车中
        if (cartService.containsItemId(cart, itemId)) {
            cartService.incrementQuantityByItemId(cart, itemId);
        } else {
            if (user == null) {
                return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), "请先登录");
            }
            cartService.addItemToCart(cart, item);
        }

        return CommonResponse.createForSuccess("商品已添加至购物车");
    }

    @PostMapping("removeItem")
    @ResponseBody
    public CommonResponse<String> removeItem(@RequestBody Map<String, Integer> map,
                                             @SessionAttribute(name = "cart", required = false) Cart cart,
                                             @SessionAttribute(name = "loginUser", required = false) User user) {
        if (user == null) {
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), "请先登录");
        }
        if (cart == null) {
            return CommonResponse.createForError(ResponseCode.NEED_LOGIN.getCode(), "请先登录");
        }
        int itemId = map.get("itemId");
        cartService.removeItemFromCart(cart, itemId);

        return CommonResponse.createForSuccess("商品已从购物车移除");
    }

}
