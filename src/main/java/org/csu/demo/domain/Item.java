package org.csu.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/*商品*/
@Data
@AllArgsConstructor
@Builder
public class Item implements Serializable {
    private int id;
    private String name;
    private ProductType type;
    private String URL;
    private int price;
    private String description;
    private int businessId;
    private int remainNumb;
}
