package org.csu.demo.service;

import org.csu.demo.domain.Cart;
import org.csu.demo.domain.CartItem;
import org.csu.demo.domain.Item;
import org.csu.demo.persistence.CartDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("CartService")
public class CartService {
    @Autowired
    private CartDao cartDao;
    @Autowired
    private ItemService itemService;

    public Cart updateCart(int userid, int itemid, int itemNum, Cart cart) {
        cartDao.coverCartItem(userid, itemid);
        if (itemNum != 0) {
            cartDao.executeAddCart(userid, itemid, itemNum);
        }
        cart.setItemList(cartDao.searchUserCartItems(userid));
        int totalCount = getTotalCount(cart.getItemList());
        int totalPrice = cart.getTotalPrice() / cart.getTotalCount() * totalCount;
        cart.setTotalPrice(totalPrice);
        cart.setTotalCount(totalCount);
        return cart;
    }

    public Cart getCart(int userid) {
        List<CartItem> cartItemList = cartDao.searchUserCartItems(userid);
        return Cart.builder().itemList(cartItemList)
                .userId(userid)
                .totalCount(getTotalCount(cartItemList))
                .totalPrice(getTotalPrice(cartItemList))
                .build();
    }

    public int getTotalCount(List<CartItem> cartList) {
        int totalCount = 0;
        for (CartItem cartItem : cartList) {
            totalCount += cartItem.getItemNum();
        }
        return totalCount;
    }

    public int getTotalPrice(List<CartItem> cartList) {
        int totalPrice = 0;
        for (CartItem cartItem : cartList) {
            totalPrice += cartItem.getItemNum() * itemService.getItemPrice(cartItem.getItemID());
        }
        return totalPrice;
    }

    public boolean containsItemId(Cart cart, int itemid) {
        for (CartItem cartItem : cart.getItemList()) {
            if (cartItem.getItemID() == itemid) {
                return true;
            }
        }
        return false;
    }

    public CartItem getCartItemById(Cart cart, int itemid) {
        for (CartItem cartItem : cart.getItemList()) {
            if (cartItem.getItemID() == itemid) {
                return cartItem;
            }
        }
        return null;
    }

    public Cart incrementQuantityByItemId(Cart cart, int itemId) {
        for (CartItem cartItem : cart.getItemList()) {
            if (cartItem.getItemID() == itemId) {
                int userId = cart.getUserId();
                cartDao.coverCartItem(userId, itemId);
                cartDao.executeAddCart(userId, itemId, cartItem.getItemNum() + 1);
                cartItem.setItemNum(cartItem.getItemNum() + 1);
                return cart;
            }
        }
        return cart;
    }

    public Cart addItemToCart(Cart cart, Item item) {
        cart.getItemList().add(CartItem.builder()
                .itemID(item.getId())
                .itemNum(1)
                .userID(cart.getUserId())
                .name(item.getName())
                .url(item.getUrl())
                .build());
        cartDao.executeAddCart(cart.getUserId(), item.getId(), 1);
        return cart;
    }

    public Cart removeItemFromCart(Cart cart, int item) {
        cart.getItemList().remove(getCartItemById(cart, item));
        cartDao.coverCartItem(cart.getUserId(), item);
        return cart;
    }
}
