package org.csu.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {
    private int itemID;
    private int itemNum;
    private String name;
    private String url;
    private int price;
    private int userID;
}


