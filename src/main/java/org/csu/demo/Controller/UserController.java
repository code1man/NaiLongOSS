package org.csu.demo.web;

import org.csu.demo.domain.User;
import org.csu.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    private String errorValidated(String locationForm, BindingResult bindingResult, Model model) {
        StringBuilder validationErrorsMsg = new StringBuilder();
        bindingResult.getAllErrors().forEach(error -> validationErrorsMsg.append(error.getDefaultMessage()).append(','));
        model.addAttribute( locationForm + "Msg", validationErrorsMsg.substring(0, validationErrorsMsg.length() - 1));
        return locationForm;
    }
}
