package org.csu.demo.web;

import org.csu.demo.domain.User;
import org.csu.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
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
    public String getAllUsers(Model model){

        //使用业务层调用dao层查询出数据，通过model对象渲染到前台页面
        List<User> userList = userService.getAllUsers();
        model.addAttribute(userList);
        return "allUser";
    }


    //添加书籍，首先需要跳转到添加用户的表单页面
    @RequestMapping("/toAddUser")
    public String toAddUser(){

        //接收到前端请求后，跳到添加用户表单页面
        return "addUser";
    }

    //接收添加用户表单的数据，进行正式的添加用户，添加完成后，重定向到所有用户页面
    @RequestMapping("addUser")
    public String addUser(User user){

        userService.addUser(user);
        System.out.println(user.toString());
        return "redirect:/admin/allUser";
    }

    //更新用户，与添加用户流程基本一样
    @RequestMapping("toUpdateUser")
    public String toUpdateUser(Model model,int id){

        User user = userService.getUser(id);
        model.addAttribute("user",user);
        //跳转到用户修改页面，同时将要修改的用户的信息传递过去
        return "updateUser";
    }

    //正式更新用户
    @RequestMapping("updateUser")
    public String updateUser(User user){

        System.out.println(user.toString());
        userService.updateUser(user, user.getUsername(), user.getPassword(), user.getEmail(), user.getAge());
        System.out.println(user.getId());
        return "redirect:/admin/allUser";
    }


    //删除就直接删除用户即可
    @RequestMapping("deleteUser")
    public String delUser(int id){

        userService.deleteUser(id);
        return "redirect:/admin/allUser";
    }


    //查询用户 根据用户名查询
    @RequestMapping("/queryUser")
    public  String queryUser(String userName,Model model){

        User user = userService.getUserByUsername(userName);
        model.addAttribute(user);
        return "allUser";
    }


    @PostMapping("/users/{id}/freeze")
    public ResponseEntity<?> freezeUser(@PathVariable int id, @RequestBody FreezeRequest request) {
        userService.freezeUser(id, request.getReason());
        return ResponseEntity.ok("User frozen successfully");
    }

    @PostMapping("/users/{id}/unfreeze")
    public ResponseEntity<?> unfreezeUser(@PathVariable int id) {
        userService.unfreezeUser(id);
        return ResponseEntity.ok("User unfrozen successfully");
    }
}
