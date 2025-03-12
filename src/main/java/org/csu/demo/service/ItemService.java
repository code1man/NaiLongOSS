package org.csu.demo.service;

import org.csu.demo.domain.Item;
import org.csu.demo.persistence.ItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ItemService")
public class ItemService {
    @Autowired
    private ItemDao itemDao;

    public List<Item> getItemsByProductId(int productId) {
        return itemDao.getItemListByProduct(productId);
    }

    public List<Item> SearchItems(String keyword) {
        return itemDao.SearchItems(keyword);
    }

    public int getItemPrice(int itemId) {
        return itemDao.getItem(itemId).getPrice();
    }

    public Item getTtemByItemId(int itemId) {
        return itemDao.getItem(itemId);
    }
}
