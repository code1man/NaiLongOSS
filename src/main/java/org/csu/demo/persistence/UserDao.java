package org.csu.demo.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.csu.demo.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserDao {
    List<User> getAllUsers();

    User getUser(@Param("id") int id);

    int addUser(User user);

    int addMerchant(User user);

    int addStatus(User user);

    User findByUsername(@Param("username") String username);

    User getUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    User getUserByUsername(@Param("username") String username);

    void updateUser(@Param("id") int id,
            @Param("username") String username,
            @Param("password") String password,
            @Param("email") String email,
            @Param("age") int age);

    boolean deleteUser(@Param("id") int id);

    // 获取完整的用户信息，包括状态和商家信息
    List<User> getAllUsersWithDetails();
}
