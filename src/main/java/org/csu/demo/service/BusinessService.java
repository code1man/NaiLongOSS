package org.csu.demo.service;

import org.csu.demo.domain.Item;
import org.csu.demo.persistence.BusinessDao;
import org.csu.demo.persistence.ItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("BusinessService")
public class BusinessService {
    @Autowired
    private BusinessDao businessDao;
    @Autowired
    private ItemDao itemDao;

    public List<Item> findAll() {
        return businessDao.getAllBusinessItems();
    }

    public int insertItem(Item item) {
        int tmp1 = businessDao.insertBusinessItem(item);
        int tmp2 = itemDao.insertItem(item);
        return (tmp1 + tmp2) - 1;
    }

    public int deleteItem(int itemId) {
        int tmp1 = businessDao.deleteBusinessItem(itemId);
        int tmp2 = itemDao.deleteItem(itemId);
        return (tmp1 + tmp2) - 1;
    }

    public int updateItem(Item item) {
        return businessDao.updateBusinessItem(item);
    }

    public int getItemCount(int itemId) {
        return businessDao.queryBusinessItemCount(itemId);
    }
}
