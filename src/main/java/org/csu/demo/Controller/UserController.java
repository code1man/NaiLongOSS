package org.csu.demo.Controller;

import org.csu.demo.domain.User;
import org.csu.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/doLogin")
    public String login(@ModelAttribute User user,
                           BindingResult bindingResult,
                           Model model) {
        System.out.println("login----------------");
        User loginUser=null;
        if (bindingResult.hasErrors()) {
//            return errorValidated("login", bindingResult, model);
            System.out.println(bindingResult.getAllErrors());
            model.addAttribute("loginMsg", "账号或密码为空");
            return "login";
        } else {
            loginUser = userService.login(user.getUsername(), user.getPassword());
        }

        if (loginUser != null) {
            model.addAttribute("loginUser", loginUser);
            System.out.println("loginUser----------------");
            return "redirect:/mainForm";
        } else {
            model.addAttribute("loginMsg", "账号或密码错误");
            return "login";
        }
    }



    // 还要判断验证码是否正确
    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           RedirectAttributes redirectAttributes) {
            boolean isSuccess = userService.register(user);
            if (!isSuccess) {
                redirectAttributes.addFlashAttribute("message", "注册失败，请检查用户名或邮箱是否已被注册！");
                return "redirect:/registerForm";
            }
            redirectAttributes.addFlashAttribute("message", "注册成功，请登录！");
            // 只存在一次message
            return "redirect:/loginForm";
            // 使用重定向，防止表单重复提交
    }

    private String errorValidated(String locationForm, BindingResult bindingResult, Model model) {
        StringBuilder validationErrorsMsg = new StringBuilder();
        bindingResult.getAllErrors().forEach(error -> validationErrorsMsg.append(error.getDefaultMessage()).append(','));
        model.addAttribute( locationForm + "Msg", validationErrorsMsg.substring(0, validationErrorsMsg.length() - 1));
        return locationForm;
    }
}
