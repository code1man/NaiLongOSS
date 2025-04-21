package org.csu.demo.domain.VO;

import lombok.Data;
import org.csu.demo.domain.Address;
import org.csu.demo.domain.CartItem;

import java.util.List;

@Data
public class CartOrder {

    private List<CartItem> cartItemList;

    private List<Address> addressList;
}
