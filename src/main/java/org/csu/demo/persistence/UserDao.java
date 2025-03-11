package org.csu.demo.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.csu.demo.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Repository
@Mapper
public interface UserDao {
    List<User> getAllUsers();

    User getUser(@Param("id") int id);

    int addUser(User user);

    User getUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    User getUserByUsername(@Param("username") String username);

    boolean updateUser(@Param("id") int id,
                       @Param("username") String username,
                       @Param("password") String password,
                       @Param("email") String email,
                       @Param("age") int age);

    boolean deleteUser(@Param("id") int id);

    List<User> getUserByIsOnlineTrue();

    List<User> getUserByIsOnlineFalse();

    List<User> getUserByIsFrozenTrue();

    List<User> getUserByIsFrozenFalse();

    int countAllUsers();

    int countOnlineUsers();

    int countFrozenUsers();

    String getFrozenReason(@Param("id") int id);


}
