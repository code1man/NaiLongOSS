package org.csu.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {
    //对应的orderId
    private String order_id;
    private int itemID;
    private int itemNum;
    private String name;
    private String url;
    private int price;
    private int userID;
    private int status;

}
