package org.csu.demo.service;
import lombok.extern.log4j.Log4j2;
import org.csu.demo.domain.Cart;
import org.csu.demo.domain.Category;
import org.csu.demo.domain.Item;
import org.csu.demo.domain.Product;
import org.csu.demo.persistence.CartDao;
import org.csu.demo.persistence.CategoryDao;
import org.csu.demo.persistence.ItemDao;
import org.csu.demo.persistence.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Service("CatalogService")
@Log4j2
public class CatalogService {
    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ItemDao itemDao;

    public List<Category> getCategories() {
        return categoryDao.getCategoryList();
    }

    public Category getCategory(int categoryId) {
        return categoryDao.getCategoryById(categoryId);
    }

    public Product getProduct(int productId) {
        return productDao.getProductById(productId);
    }

    public List<Product> getProductListByCategory(int categoryId) {
        return productDao.getProductListByCategory(categoryId);
    }

    public List<Item> getItemListByProduct(int productId) {
        return itemDao.getItemListByProduct(productId);
    }
}