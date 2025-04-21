package org.csu.demo.service;

import lombok.extern.log4j.Log4j2;
import org.csu.demo.domain.Cart;
import org.csu.demo.domain.CartItem;
import org.csu.demo.domain.Item;
import org.csu.demo.persistence.CartDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("CartService")
@Log4j2
public class CartService {
    @Autowired
    private CartDao cartDao;
    @Autowired
    private ItemService itemService;

    public void updateCart(int userid, int itemid, int itemNum) {
        cartDao.coverCartItem(userid, itemid);
        if (itemNum != 0) {
            cartDao.executeAddCart(userid, itemid, itemNum);
        }
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

    public boolean containsItemId(int userID, int itemID) {
        for (CartItem item : cartDao.searchUserCartItems(userID)){
            if (item.getItemID() == itemID){
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

    public void incrementQuantityByItemId(int userId, int itemId) {
        int itemCount = cartDao.getCartCount(userId,itemId);
        cartDao.coverCartItem(userId, itemId);
        cartDao.executeAddCart(userId, itemId, itemCount + 1);
    }

    public void addItemToCart(int userID, int itemID) {
        cartDao.executeAddCart(userID, itemID, 1);
    }

    public void removeItemFromCart(int userId, int item) {
        cartDao.executeRemoveCart(userId, item);
    }
}
