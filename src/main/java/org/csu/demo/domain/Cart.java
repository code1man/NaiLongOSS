package org.csu.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;

/*用户的购物车，也可以用在用户管理中*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {
    private List<CartItem> itemList = new ArrayList<CartItem>();
    private int userId;
    private int totalPrice;
    private int totalCount;
}


