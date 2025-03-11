package org.csu.demo.service;

import org.csu.demo.domain.User;
import org.csu.demo.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    public boolean updateUser(User user, String username, String password, String email, int age) {
        return userDao.updateUser(user.getId(), username, password, email, age);
    }

    public boolean checkUsername(String username) {
        return userDao.getUserByUsername(username) != null;
    }
}
