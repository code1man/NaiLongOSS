package org.csu.demo.domain;

import lombok.*;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*商品大类*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class  Product {
    private int id;
    private String description;
    private int category_id;

    private List<Item> items;
}
