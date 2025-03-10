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
    private ProductType productType;
    private int ProductNumber = 0;
    private final List<Item> items = new ArrayList<Item>();

    public Product() {
    }

    // 初始化
    public Product(ProductType productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productType.getDescription();
    }

    public void addProduct(Item item) {
        items.add(item);
        ProductNumber++;
    }

    public void deleteProduct(Item item) {
        items.remove(item);
        ProductNumber--;
    }

    public void setItems(List<Item> items) {
        this.items.clear();
        this.items.addAll(items);
    }
}
