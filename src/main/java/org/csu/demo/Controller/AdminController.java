package org.csu.demo.Controller;

import org.csu.demo.domain.User;
import org.csu.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @RequestMapping("/allUser")
    public String getAllUsers(Model model) {

        //使用业务层调用dao层查询出数据，通过model对象渲染到前台页面
        List<User> userList = userService.getAllUsers();
        model.addAttribute(userList);
        return "allUser";
    }


    //添加书籍，首先需要跳转到添加用户的表单页面
    @RequestMapping("/toAddUser")
    public String toAddUser() {

        //接收到前端请求后，跳到添加用户表单页面
        return "addUser";
    }

    //接收添加用户表单的数据，进行正式的添加用户，添加完成后，重定向到所有用户页面
    @RequestMapping("addUser")
    public String addUser(User user) {

        userService.addUser(user);
        System.out.println(user.toString());
        return "redirect:/admin/allUser";
    }

    //更新用户，与添加用户流程基本一样
    @RequestMapping("toUpdateUser")
    public String toUpdateUser(Model model, int id) {

        User user = userService.getUser(id);
        model.addAttribute("user", user);
        //跳转到用户修改页面，同时将要修改的用户的信息传递过去
        return "updateUser";
    }

    //正式更新用户
    @RequestMapping("updateUser")
    public String updateUser(User user) {

        System.out.println(user.toString());
        userService.updateUser(user, user.getUsername(), user.getPassword(), user.getEmail(), user.getAge());
        System.out.println(user.getId());
        return "redirect:/admin/allUser";
    }


    //删除就直接删除用户即可
    @RequestMapping("deleteUser")
    public String delUser(int id) {

        userService.deleteUser(id);
        return "redirect:/admin/allUser";
    }


    //查询用户 根据用户名查询
    @RequestMapping("/queryUser")
    public String queryUser(String userName, Model model) {

        User user = userService.getUserByUsername(userName);
        model.addAttribute(user);
        return "allUser";
    }

    @Transactional
    @PostMapping("/users/freeze/{id}/{frozen_reason}")
    public ResponseEntity<?> freezeUser(@PathVariable int id, @PathVariable String frozen_reason) {
        userService.freezeUser(id, frozen_reason);
        return ResponseEntity.ok("已成功封禁该用户");
    }

    @PostMapping("/users/unfreeze/{id}")
    public ResponseEntity<?> unfreezeUser(@PathVariable int id) {
        userService.unfreezeUser(id);
        return ResponseEntity.ok("已成功将该用户解封");
    }

    @GetMapping("/users/status")
    public ResponseEntity<?> getAllUserStatus() {
        List<User> users = userService.getAllUserStatus();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/frozen")
    public ResponseEntity<?> getAllFrozenUsers() {
        List<User> users = userService.getUserByIsFrozenTrue();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/online")
    public ResponseEntity<?> getAllOnlineUsers() {
        List<User> users = userService.getUserByIsOnlineTrue();
        return ResponseEntity.ok(users);
    }

    //查询所有未上线用户
    @GetMapping("/users/offline")
    public ResponseEntity<?> getAllOfflineUsers() {
        List<User> users = userService.getUserByIsOnlineFalse();
        return ResponseEntity.ok(users);
    }

    //查询所有用户数量
    @GetMapping("/users/count")
    public ResponseEntity<?> getAllUserCount() {
        int count = userService.countAllUsers();
        return ResponseEntity.ok(count);
    }

    //查询所有封禁用户
    @GetMapping("/users/frozen/count")
    public ResponseEntity<?> getAllFrozenUserCount() {
        int count = userService.countFrozenUsers();
        return ResponseEntity.ok(count);
    }

    //查询所有在线用户
    @GetMapping("/users/online/count")
    public ResponseEntity<?> getAllOnlineUserCount() {
        int count = userService.countOnlineUsers();
        return ResponseEntity.ok(count);
    }


    //查询封禁原因
    @GetMapping("/users/frozen/reason/{id}")
    public ResponseEntity<?> getFrozenReason(@PathVariable int id) {
        String reason = userService.getFrozenReason(id);
        return ResponseEntity.ok(reason);
    }

    //查询所有商家详情
    @GetMapping("/users/merchants")
    public ResponseEntity<?> getAllMerchants() {
        List<User> merchants = userService.getAllMerchants();
        return ResponseEntity.ok(merchants);
    }


}