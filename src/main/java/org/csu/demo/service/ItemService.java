package org.csu.demo.service;

import lombok.extern.log4j.Log4j2;
import org.csu.demo.domain.Item;
import org.csu.demo.persistence.ItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ItemService")
@Log4j2
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

    public Item getItemByItemId(int itemId) {
        return itemDao.getItem(itemId);
    }

    public int getItemIdByName(String itemName) {return itemDao.getItemId(itemName);}
}
