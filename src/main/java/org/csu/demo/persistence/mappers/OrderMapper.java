package org.csu.demo.persistence.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.demo.domain.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper extends BaseMapper<Order> {
}
