package org.csu.demo.service;

import lombok.extern.log4j.Log4j2;
import org.csu.demo.domain.Item;
import org.csu.demo.persistence.BusinessDao;
import org.csu.demo.persistence.ItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service("BusinessService")
@Log4j2
public class BusinessService {
    @Autowired
    private BusinessDao businessDao;
    @Autowired
    private ItemDao itemDao;

    /*获取所有商品的信息*/
    public List<Item> findAll() {
        return businessDao.getAllBusinessItems();
    }

    public List<Item> getBusinessItemById(int itemId){
        return  itemDao.getItemListByProduct(itemId);
    }
    public List<Item> getBusinessItemByIdIgnoreList(int itemId){
        return itemDao.getItemListByProductAndIgnoreListing(itemId);
    }
    /*插入新的商品*/
    public int insertItem(Item item) {
        int tmp1 = businessDao.insertBusinessItem(item);
        int tmp2 = itemDao.insertItem(item);
        return (tmp1 + tmp2) - 1;
    }

    /*上下架商品*/
    public void updateProductAvailability(int itemId, boolean availability) {
        businessDao.updateProductAvailability(itemId, availability);
    }

    /*删除商品*/
    public int deleteItem(int itemId) {
        int tmp1 = businessDao.deleteBusinessItem(itemId);
        int tmp2 = itemDao.deleteItem(itemId);
        return (tmp1 + tmp2) - 1;
    }

    /*查询剩余商品数量*/
    public int getItemCount(int itemId) {
        return businessDao.queryBusinessItemCount(itemId);
    }

    /*更新商品各种信息*/
    public int updateItem( @RequestBody Item item) {
        return businessDao.updateBusinessItemById(item);
    }


    public List<Item> getBusinessItemByIdAndMerchantId(int product_Id, int merchantId) {
        return businessDao.getBusinessItemByIdAndMerchantId(product_Id, merchantId);
    }
}
