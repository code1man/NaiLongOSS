package org.csu.demo.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.csu.demo.domain.User;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserStatusDao {
    void addStatus(User user);
}
