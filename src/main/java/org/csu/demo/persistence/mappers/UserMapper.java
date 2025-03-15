package org.csu.demo.persistence.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.demo.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
}
