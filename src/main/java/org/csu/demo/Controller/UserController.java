package org.csu.demo.Controller;

import org.csu.demo.common.CommonResponse;
import org.csu.demo.domain.Cart;
import org.csu.demo.domain.User;
import org.csu.demo.service.CartService;
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
@SessionAttributes(value = {"loginUser","message","cart"})//登录成功后，将loginUser对象放入session中，供其他页面使用,只有先放在modelAttribute中，才能在页面中获取到
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;

    @GetMapping("/loginForm")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/registerForm")
    public String RegisterForm() {
        return "register";
    }

    @PostMapping("/doLogin")//@ModelAttribute User user用来获取表单数据，绑定到User对象上，BindingResult用来获取验证结果
    public String login(@ModelAttribute User user,
                           BindingResult bindingResult,
                           Model model) {
        User loginUser;
        Cart cart;
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            model.addAttribute("loginMsg", "账号或密码为空");
            return "login";
        } else {
            loginUser = userService.login(user.getUsername(), user.getPassword());
        }

        if (loginUser != null) {
            if(loginUser.is_frozen()){
                model.addAttribute("loginMsg", "账号已被冻结，原因：" + userService.getFrozenReason(loginUser.getId()));
                return "login";
            }
            if(loginUser.getCredit()<60&&loginUser.getResponsibility().equals("merchant"))
            {
                model.addAttribute("loginMsg", "您的信誉过低，无法登录，请规范行为");
                return "login";
            }
            cart = cartService.getCart(loginUser.getId());
            model.addAttribute("loginUser", loginUser);
            model.addAttribute("cart", cart);
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



    @GetMapping("/usernameIsExist")
    @ResponseBody
    public CommonResponse<Object> usernameIsExist(@RequestParam String username) {
        boolean isExist = userService.checkUsername(username);
        if(isExist) {
            return CommonResponse.createForError("用户名存在");
        }
        return CommonResponse.createForSuccess();
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
