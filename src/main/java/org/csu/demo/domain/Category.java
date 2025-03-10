package org.csu.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Category implements Serializable {
    private CategoryType categoryType;
    private List<ProductType> products;

    public Category(CategoryType categoryType) {
        this.categoryType = categoryType;

        products = CategoryType.getContainingProductTypes(categoryType);
    }

    public String getCategoryName() {
        return categoryType.getDescription();
    }

    public void addProductType(ProductType productType) {
        products.add(productType);
    }

    public void deleteProductType(ProductType productType) {
        products.remove(productType);
    }
}
