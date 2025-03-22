package org.csu.demo.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.csu.demo.domain.CartItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CartDao {
    void executeAddCart(int userID, int itemID, int itemNum);
    void executeRemoveCart(int useID, int itemID);
    List<CartItem> searchUserCartItems(int userID);
    void coverCartItem(int userID, int itemID);
}
