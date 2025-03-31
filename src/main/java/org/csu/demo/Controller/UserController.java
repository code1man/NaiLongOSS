package org.csu.demo.Controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.csu.demo.common.CommonResponse;
import org.csu.demo.domain.Cart;
import org.csu.demo.domain.User;
import org.csu.demo.exception.LoginException;
import org.csu.demo.service.CartService;
import org.csu.demo.service.OrderService;
import org.csu.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.SocketTimeoutException;
import java.util.List;

@Controller
@Validated
@SessionAttributes(value = { "loginUser", "message", "cart", "captcha", "orderList" }) // 登录成功后，将loginUser对象放入session中，供其他页面使用,只有先放在modelAttribute中，才能在页面中获取到
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @ModelAttribute("captcha")
    public String getCaptcha(HttpSession session) {
        return (String) session.getAttribute("captcha");
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/registerForm")
    public String RegisterForm() {
        return "register";
    }

    @PostMapping("/doLogin") // @ModelAttribute User user用来获取表单数据，绑定到User对象上，BindingResult用来获取验证结果
    public String login(@Valid @ModelAttribute User user,
/*            BindingResult bindingResult,*/
            Model model) {
        User loginUser;
        Cart cart = new Cart();
/*        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            model.addAttribute("loginMsg", "账号或密码为空");
            return "login";
        } */
        loginUser = userService.login(user.getUsername(), user.getPassword());
        if (loginUser != null) {
            if (loginUser.getResponsibility().equals("merchant")) {
                loginUser.setCredit(userService.getMerchantCredit(loginUser.getId()));
                if (loginUser.getCredit() < 60) {
                    throw new LoginException("LOGIN_FAILED", "您的信誉分太低");
                }
            }
            if (loginUser.getResponsibility().equals("user")) {
                cart = cartService.getCart(loginUser.getId());
            }
            model.addAttribute("loginUser", loginUser);
            model.addAttribute("cart", cart);
            // 把买家相关订单放到session
            model.addAttribute("orderList", orderService.getOrderListByClient(loginUser.getId(), 0));

            if ("user".equals(loginUser.getResponsibility())) {
                return "redirect:/mainForm";
            } else if ("merchant".equals(loginUser.getResponsibility())) {
                return "redirect:/merchantForm";
            } else if ("admin".equals(loginUser.getResponsibility())) {
                return "redirect:/ManagerForm";
            } else {
                model.addAttribute("loginMsg", loginUser.getResponsibility());
                return "redirect:/loginForm";
            }
        } else {
            throw new LoginException("LOGIN_FAILED", "账号或密码错误");
        }
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, @RequestParam String captcha,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (model.getAttribute("captcha") == null || !captcha.equals(model.getAttribute("captcha"))) {
            redirectAttributes.addFlashAttribute("message", "验证码错误");
            return "redirect:/registerForm";
        }

        boolean isSuccess = userService.register(user);
        if (!isSuccess) {
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
        if (isExist) {
            return CommonResponse.createForError("用户名存在");
        }
        return CommonResponse.createForSuccess();
    }

    @RequestMapping("/main")
    public String mainForm() {
        return "main";
    }

    @RequestMapping("/merchantForm")
    public String merchantForm() {
        System.out.println("我被调用了");
        return "ProductMerchantManage";
    }


    @RequestMapping("/ManagerForm")
    public String ManagerForm(Model model) {
        List<User> userList = userService.getAllUsersWithDetails();
        System.out.println(userList);
        // 为每个商家计算星级
        for (User user : userList) {
            if ("merchant".equals(user.getResponsibility())) {
                user.setStars(userService.calculateStars(user.getMerchantCredit()));
            }
        }
        model.addAttribute("userList", userList);
        return "UserManage";
    }

    private String errorValidated(String locationForm, BindingResult bindingResult, Model model) {
        StringBuilder validationErrorsMsg = new StringBuilder();
        bindingResult.getAllErrors()
                .forEach(error -> validationErrorsMsg.append(error.getDefaultMessage()).append(','));
        model.addAttribute(locationForm + "Msg", validationErrorsMsg.substring(0, validationErrorsMsg.length() - 1));
        return locationForm;
    }

    /// 演示模块
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/timeout")
    public String simulateTimeout() throws SocketTimeoutException {
        try {
            // 这里访问一个不存在的 IP，模拟请求超时
            restTemplate.getForObject("http://10.255.255.1", String.class);
        } catch (ResourceAccessException e) {
            if (e.getCause() instanceof SocketTimeoutException) {
                throw new SocketTimeoutException("请求超时");
            }
        }
        return "请求成功";
    }

    @GetMapping("/errorSystem")
    public int errorSystem() {
        return 1/0;
    }

}
