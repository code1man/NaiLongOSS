package org.csu.demo.persistence;

import org.csu.demo.domain.Product;
import org.csu.demo.domain.ProductType;

import java.sql.SQLException;
import java.util.List;

public interface ProductDao {
    List<ProductType> getProductListByCategory(String categoryId);

    ProductType getProduct(int productId);

    List<Product> searchProductList(String keywords);
}
