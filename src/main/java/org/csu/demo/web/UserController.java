package org.csu.demo.web;

import org.csu.demo.domain.User;
import org.csu.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Validated
@SessionAttributes(value = {"loginUser"})
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/loginForm")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/registerForm")
    public String RegisterForm() {
        return "register";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute User user,
                           BindingResult bindingResult,
                           Model model) {
        System.out.println(user.getUsername() + ',' + user.getPassword());
        User loginUser;
        if (bindingResult.hasErrors()) {
            return errorValidated("login", bindingResult, model);
        } else {
            loginUser = userService.login(user.getUsername(), user.getPassword());
        }

        if (loginUser != null) {
            model.addAttribute("loginUser", loginUser);
            return "main";
        } else {
            model.addAttribute("loginMsg", "账号或密码错误");
            return "login";
        }
    }

    @PostMapping("/register")
    public String register(@Validated @ModelAttribute User user,
                           BindingResult bindingResult,
                           Model model) {
        System.out.println(user.getUsername() + ',' + user.getPassword());

        if (bindingResult.hasErrors()) {
            return errorValidated("register", bindingResult, model);
        } else {
            model.addAttribute("registerMsg", userService.register(user) ? "注册成功！" : "注册失败！");
        }

        return "register";
    }
//BIAOJI
    @RequestMapping("/allUser")
    public String  selectAllUser(Model model){

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
        return "redirect:/user/allUser";
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
        userService.updateUser(user,user.getUsername(),user.getPassword(),user.getEmail(),user.getAge());
        System.out.println(user.getId());
        return "redirect:/user/allUser";
    }


    //删除就直接删除用户即可
    @RequestMapping("delUser")
    public String delUser(int id){

        userService.deleteUser(id);
        return "redirect:/user/allUser";
    }


    //查询用户 根据用户名查询
    @RequestMapping("/queryUser")
    public  String queryUser(String userName,Model model){

        User userList = userService.getUserByUsername(userName);
        model.addAttribute(userList);
        return "allUser";
    }
//BIAOJIEND
    private String errorValidated(String locationForm, BindingResult bindingResult, Model model) {
        StringBuilder validationErrorsMsg = new StringBuilder();
        bindingResult.getAllErrors().forEach(error -> validationErrorsMsg.append(error.getDefaultMessage()).append(','));
        model.addAttribute( locationForm + "Msg", validationErrorsMsg.substring(0, validationErrorsMsg.length() - 1));
        return locationForm;
    }
}
