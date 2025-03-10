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
@SessionAttributes(value = {"loginUser","message"})
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
    public String login(@ModelAttribute User user,
                           BindingResult bindingResult,
                           Model model) {
        User loginUser;
        if (bindingResult.hasErrors()) {
            return errorValidated("login", bindingResult, model);
        } else {
            loginUser = userService.login(user.getUsername(), user.getPassword());
        }

        if (loginUser != null) {
            model.addAttribute("loginUser", loginUser);
            return "redirect:/main";
        } else {
            model.addAttribute("loginMsg", "账号或密码错误");
            return "login";
        }
    }



    // 还要判断验证码是否正确
    @PostMapping("/register")
    public String register(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email, @RequestParam("age") int age, @RequestParam("is_admin") String pro,
                           Model model) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setAge(age);
            user.setResponsibility(pro);
            boolean isSuccess = userService.register(user);
            if (!isSuccess) {
                model.addAttribute("message", "注册失败，请检查用户名或邮箱是否已被注册！");
                return "redirect:/registerForm";
            }
            model.addAttribute("message", "注册成功，请登录！");
            return "redirect:/loginForm";
            // 使用重定向，防止表单重复提交
    }

    @RequestMapping("/main")
    public String mainForm() {
        return "main";
    }


    private String errorValidated(String locationForm, BindingResult bindingResult, Model model) {
        StringBuilder validationErrorsMsg = new StringBuilder();
        bindingResult.getAllErrors().forEach(error -> validationErrorsMsg.append(error.getDefaultMessage()).append(','));
        model.addAttribute( locationForm + "Msg", validationErrorsMsg.substring(0, validationErrorsMsg.length() - 1));
        return locationForm;
    }
}
