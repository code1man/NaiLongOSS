package org.csu.demo.service;

import org.csu.demo.domain.User;
import org.csu.demo.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("UserService")
public class UserService {
    @Autowired
    private UserDao userDao;             //提供了对数据库操作的方法

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User login(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public boolean register(User user) {
        // 加密密码
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        // 调用 MyBatis Mapper 保存用户信息
        return this.userDao.addUser(user) == 1;
    }

    public void updateUser(User user, String username, String password, String email, int age) {
        userDao.updateUser(user.getId(), username, password, email, age);
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

    public List<User> getUserByIsOnlineTrue(){return userDao.getUserByIsOnlineTrue();}

    public List<User> getUserByIsOnlineFalse(){return userDao.getUserByIsOnlineFalse();}

    public List<User> getUserByIsFrozenTrue(){return userDao.getUserByIsFrozenTrue();}

    public List<User> getUserByIsFrozenFalse(){return userDao.getUserByIsFrozenFalse();}

    public int countAllUsers(){return userDao.countAllUsers();}

    public int countOnlineUsers(){return userDao.countOnlineUsers();}

    public int countFrozenUsers(){return userDao.countFrozenUsers();}

    public String getFrozenReason(int id){return userDao.getFrozenReason(id);}

    public void freezeUser(int id, String frozen_reason){userDao.freezeUser(id, frozen_reason);}

    public void unfreezeUser(int id){userDao.unfreezeUser(id);}

    public List<User> getAllUserStatus(){return userDao.getAllUserStatus();}

    public List<User> getAllMerchants(){return userDao.getAllMerchants();}
}
