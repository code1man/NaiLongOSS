package org.csu.demo.service;

import org.csu.demo.domain.User;
import org.csu.demo.persistence.AdminDao;
import org.csu.demo.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("UserService")
public class UserService {
    @Autowired
    private UserDao userDao;
    private AdminDao adminDao;//提供了对数据库操作的方法

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

    public List<User> getUserByIsOnlineTrue(){return adminDao.getUserByIsOnlineTrue();}

    public List<User> getUserByIsOnlineFalse(){return adminDao.getUserByIsOnlineFalse();}

    public List<User> getUserByIsFrozenTrue(){return adminDao.getUserByIsFrozenTrue();}

    public List<User> getUserByIsFrozenFalse(){return adminDao.getUserByIsFrozenFalse();}

    public int countAllUsers(){return adminDao.countAllUsers();}

    public int countOnlineUsers(){return adminDao.countOnlineUsers();}

    public int countFrozenUsers(){return adminDao.countFrozenUsers();}

    public String getFrozenReason(int id){return adminDao.getFrozenReason(id);}

    public void freezeUser(int id, String frozen_reason){adminDao.freezeUser(id, frozen_reason);}

    public void unfreezeUser(int id){adminDao.unfreezeUser(id);}

    public List<User> getAllUserStatus(){return adminDao.getAllUserStatus();}

    public List<User> getAllMerchants(){return adminDao.getAllMerchants();}

    public void creditDecrease(int merchantId){adminDao.creditDecrease(merchantId);}

    public void creditIncrease(int merchantId){adminDao.creditIncrease(merchantId);}

    public void creditSetUnqualified(int merchantId){adminDao.creditSetUnqualified(merchantId);}

    public void creditSetQualified(int merchantId){adminDao.creditSetQualified(merchantId);}

    public void setIsOnlineFalse(int id){adminDao.setIsOnlineFalse(id);}

    public void setIsOnlineTrue(int id){adminDao.setIsOnlineTrue(id);}
}
