package org.csu.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*商品大类*/
@Data
@AllArgsConstructor
@Builder
public class Product implements Serializable {
    private int product_id;
    private int ProductNumber = 0;
    private final List<Item> items = new ArrayList<Item>();

    public Product() {
    }
}
