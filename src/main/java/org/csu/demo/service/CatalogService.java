package org.csu.demo.service;
import org.csu.demo.domain.Category;
import org.csu.demo.domain.Item;
import org.csu.demo.domain.Product;
import org.csu.demo.persistence.CategoryDao;
import org.csu.demo.persistence.ItemDao;
import org.csu.demo.persistence.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("CatalogService")
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


    private static final String Add_Item = "INSERT INTO cart (userID,ItemID,ItemNum) VALUES(?,?,?)";
    private static final String Remove_Item = "DELETE FROM cart where ItemID=?";
    private static final String Search_Item = "SELECT * FROM cart WHERE userID = ? AND isCovered = false AND isDeleted = false";
    private static final String Update_Item = "UPDATE cart SET isCovered=true WHERE userID=? AND ItemID=?";



}