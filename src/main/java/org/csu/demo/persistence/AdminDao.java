package org.csu.demo.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.csu.demo.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository//@Repository表示这个类是一个持久层类，用于持久化操作
@Mapper
public interface AdminDao {
    List<User> getUserByIsOnlineTrue();

    List<User> getUserByIsOnlineFalse();

    List<User> getUserByIsFrozenTrue();

    List<User> getUserByIsFrozenFalse();

    int countAllUsers();

    int countFrozenUsers();

    int countOnlineUsers();

    String getFrozenReason(@Param("id") int id);

    void freezeUser(@Param("id") int id, @Param("frozen_reason") String frozen_reason);

    void unfreezeUser(@Param("id") int id);

    List<User> getAllUserStatus();

    List<User> getAllMerchants();

    void creditDecrease(@Param("merchantId") int merchantId);

    void creditIncrease(@Param("merchantId") int merchantId);

    void creditSetUnqualified(@Param("merchantId") int merchantId);

    void creditSetQualified(@Param("merchantId") int merchantId);

    void setIsOnlineFalse(@Param("id") int id);

    void setIsOnlineTrue(@Param("id") int id);


}
