package org.csu.demo.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.csu.demo.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BusinessDao {
    // 查询所有商品库存信息
    List<Item> getAllBusinessItems();

    // 根据 itemId 查询库存信息
    Item getBusinessItemById(int itemId);

    // 根据 itemId 修改信息
    Item updateBusinessItemById(int itemId, Item item);

    // 插入新的商品库存信息
    int insertBusinessItem(Item item);

    // 更新商品库存
    int updateBusinessItem(Item item);

    // 删除库存记录
    int deleteBusinessItem(int itemId);

    // 获取库存数量
    int queryBusinessItemCount(int itemId);

    // 通过商品id查商家id
    int getSupplierByItemId(int itemId);
}
