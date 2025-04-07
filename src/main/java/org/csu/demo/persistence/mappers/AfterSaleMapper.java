package org.csu.demo.persistence.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.demo.domain.AfterSale;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

@Repository
public interface AfterSaleMapper extends BaseMapper<AfterSale> {
}
