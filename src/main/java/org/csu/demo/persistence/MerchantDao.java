package org.csu.demo.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MerchantDao {
    int addMerchant(int id);
}
