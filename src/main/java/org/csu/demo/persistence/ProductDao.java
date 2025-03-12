package org.csu.demo.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.csu.demo.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ProductDao {
    List<Product> getProductListByCategory(@Param("categoryId") int categoryId);

    List<Product> getAllProducts();

    Product getProductById(@Param("productId") int productId);

    Product getProductByName(@Param("productName") String productName);
}
