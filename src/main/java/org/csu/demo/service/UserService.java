package org.csu.demo.service;

import org.csu.demo.domain.User;
import org.csu.demo.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("UserService")
public class UserService {
    @Autowired
    private UserDao userDao;             //提供了对数据库操作的方法

    public User login(String username, String password) {
        return this.userDao.getUserByUsernameAndPassword(username, password);
    }

    public boolean register(User user) {
        return this.userDao.addUser(user) == 1;
    }

    public boolean updateUser(User user, String username, String password, String email, int age) {
        return userDao.updateUser(user.getId(), username, password, email, age);
    }

    public boolean checkUsername(String username) {
        return userDao.getUserByUsername(username) != null;
    }
}
