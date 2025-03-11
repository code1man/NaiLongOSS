package org.csu.demo.service;

import org.csu.demo.domain.User;
import org.csu.demo.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public User getUser(int id) {
        return userDao.getUser(id);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        return userDao.getUserByUsernameAndPassword(username, password);
    }

    public boolean deleteUser(int id) {
        return userDao.deleteUser(id);
    }

    public int addUser(User user) {
        return userDao.addUser(user);
    }

    List<User> getUserByIsOnlineTrue(){return userDao.getUserByIsOnlineTrue();}

    List<User> getUserByIsOnlineFalse(){return userDao.getUserByIsOnlineFalse();}

    List<User> getUserByIsFrozenTrue(){return userDao.getUserByIsFrozenTrue();}

    List<User> getUserByIsFrozenFalse(){return userDao.getUserByIsFrozenFalse();}

    long countAllUsers(){return userDao.countAllUsers();}

    long countOnlineUsers(){return userDao.countOnlineUsers();}

    long countFrozenUsers(){return userDao.countFrozenUsers();}

    String getFrozenReason(int id){return userDao.getFrozenReason(id);}



}
