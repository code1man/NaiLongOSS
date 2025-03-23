package org.csu.demo.service;

import lombok.extern.log4j.Log4j2;
import org.csu.demo.domain.User;
import org.csu.demo.persistence.AdminDao;
import org.csu.demo.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("UserService")
@Log4j2
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private AdminDao adminDao;// 提供了对数据库操作的方法

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User login(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            user.setPassword(null); // 密码不返回给前端
            // System.out.println(user);
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

    public boolean checkUsername(String username) {
        return userDao.getUserByUsername(username) != null;
    }

    // 获取所有用户的完整信息
    public List<User> getAllUsersWithDetails() {
        return userDao.getAllUsersWithDetails();
    }

    // 计算商家信誉度对应的星级
    public String calculateStars(int credit) {
        int starCount = credit / 20;
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i < starCount) {
                stars.append("★");
            } else {
                stars.append("☆");
            }
        }
        return stars.toString();
    }

    public void resetPassword(int userId) {
        String defaultPassword = "123456";
        String encodedPassword = passwordEncoder.encode(defaultPassword);
        userDao.updateUserPassword(userId, encodedPassword);
    }

    // ？？？？？？ 什么时候service里写增删查改了？？？？？
    // ？？？？？？ 把Dao方法在service又写一遍？？？？？？
    // 以下都非yy写的
    public void updateUser(User user, String username, String password, String email, int age) {
        userDao.updateUser(user.getId(), username, password, email, age);
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
        int result = userDao.addUser(user);
        userDao.addStatus(user);
        if (user.getResponsibility().equals("商家"))
            return userDao.addMerchant(user);
        return result;
    }

    public List<User> getUserByIsOnlineTrue() {
        return adminDao.getUserByIsOnlineTrue();
    }

    public List<User> getUserByIsOnlineFalse() {
        return adminDao.getUserByIsOnlineFalse();
    }

    public List<User> getUserByIsFrozenTrue() {
        return adminDao.getUserByIsFrozenTrue();
    }

    public List<User> getUserByIsFrozenFalse() {
        return adminDao.getUserByIsFrozenFalse();
    }

    public int countAllUsers() {
        return adminDao.countAllUsers();
    }

    public int countOnlineUsers() {
        return adminDao.countOnlineUsers();
    }

    public int countFrozenUsers() {
        return adminDao.countFrozenUsers();
    }

    public String getFrozenReason(int id) {
        return adminDao.getFrozenReason(id);
    }

    public void freezeUser(int id, String frozen_reason) {
        adminDao.freezeUser(id, frozen_reason);
    }

    public void unfreezeUser(int id) {
        adminDao.unfreezeUser(id);
    }

    public List<User> getAllUserStatus() {
        return adminDao.getAllUserStatus();
    }

    public List<User> getAllMerchants() {
        return adminDao.getAllMerchants();
    }

    public void creditDecrease(int merchantId) {
        adminDao.creditDecrease(merchantId);
    }

    public void creditIncrease(int merchantId) {
        adminDao.creditIncrease(merchantId);
    }

    public void creditSetUnqualified(int merchantId) {
        adminDao.creditSetUnqualified(merchantId);
    }

    public void creditSetQualified(int merchantId) {
        adminDao.creditSetQualified(merchantId);
    }

    // ?????？？？？ 看不懂
    public void setIsOnlineFalse(int id) {
        adminDao.setIsOnlineFalse(id);
    }

    // ?????？？？？ 看不懂
    public void setIsOnlineTrue(int id) {
        adminDao.setIsOnlineTrue(id);
    }

}
