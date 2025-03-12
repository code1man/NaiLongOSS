package org.csu.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/*商品*/
@Data
@AllArgsConstructor
@Builder
public class Item{
    private int id;
    private String name;
    private int product_id;
    private String url;
    private int price;
    private String description;
    private int businessId;
    private int remainingNumb;
}
