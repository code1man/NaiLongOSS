package org.csu.demo.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MerchantDao {
    void addMerchant(int id);

    int gerCreditByMerchantId(int id);
}
