package org.csu.demo.service;

import lombok.extern.log4j.Log4j2;
import org.csu.demo.domain.Category;
import org.csu.demo.domain.Product;
import org.csu.demo.persistence.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*初始化商品信息*/
@Service("ProductService")
@Log4j2
public class ProductService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private ItemService itemService;

    public List<Product> getProducts() {
        List<Product> products = productDao.getAllProducts();
        for (Product product : products) {
            product.setItems(itemService.getItemsByProductId(product.getId()));
        }
        return products;
    }
}