package org.csu.demo.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.csu.demo.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ItemDao {

    List<Item> getItemListByProduct(int product_Id);

    Item getItem(int itemId);

    // 插入新的商品库存信息
    int insertItem(Item item);

    // 删除库存记录
    int deleteItem(int itemId);

    // 获取item id
    int getItemId(String itemName);

    List<Item> SearchItems(String keyword);
}
