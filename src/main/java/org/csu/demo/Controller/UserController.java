package org.csu.demo.Controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.csu.demo.common.CommonResponse;
import org.csu.demo.domain.Cart;
import org.csu.demo.domain.User;
import org.csu.demo.exception.LoginException;
import org.csu.demo.persistence.Util.CaptchaUtil;
import org.csu.demo.service.CartService;
import org.csu.demo.service.OrderService;
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
@SessionAttributes(value = { "loginUser", "message", "cart", "captcha", "orderList","businessLoginUser" }) // 登录成功后，将loginUser对象放入session中，供其他页面使用,只有先放在modelAttribute中，才能在页面中获取到
@RequestMapping("/user")
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

    @Autowired
    private HttpSession session;

    // @RequestParam从URL参数或者表单数据中获取数据
    // @PathVariable从URL中获取路径参数
    // @RequestBody从请求体中获取数据，可以封装为json对象传递
    // @ModelAttribute获取表单对象数据



    @PostMapping("login")
    @ResponseBody
    public CommonResponse<User> login(@RequestParam String username, @RequestParam String password) {
        User user = userService.login(username, password);
        if (user == null) {
            return CommonResponse.createForError("用户名或密码错误");
        }
        return CommonResponse.createForSuccess(user);
    }

    @PostMapping("register")
    @ResponseBody
    public CommonResponse<String> register(@ModelAttribute User user, @RequestParam String captcha) {
        if (userService.register(user,captcha)) {
            return CommonResponse.createForSuccess("注册成功");
        }
        return CommonResponse.createForError("验证码错误");
    }

    @PostMapping("check_username")
    @ResponseBody
    public CommonResponse<String> check_username(@RequestParam String username) {
        System.out.println(username);
        if (userService.checkUsername(username)) {
            return CommonResponse.createForError("用户名已存在");
        }
        return CommonResponse.createForSuccess();
    }

    @GetMapping("captcha")
    public void captcha(HttpServletResponse response, HttpSession session) {
        System.out.println("生成验证码");
        // 调用之前写的 CaptchaUtil 类生成验证码图片并返回验证码文本
        String captchaText = CaptchaUtil.createCaptchaImage(response);
        // 将验证码文本存入 session，用于后续验证
        session.setAttribute("captcha", captchaText);
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


    @GetMapping("/loginForm")
    public String loginForm() {
        return "login";
    }

    @RequestMapping("/merchantForm")
    public String merchantForm() {
        System.out.println("我被调用了");
        return "ProductMerchantManage";
    }

    //保证原来的网站也能登录
    @PostMapping("/doLogin") // @ModelAttribute User user用来获取表单数据，绑定到User对象上，BindingResult用来获取验证结果
    public String login1(@Valid @ModelAttribute User user,
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
            //防止相互覆盖
//            model.addAttribute("loginUser", loginUser);
            model.addAttribute("cart", cart);
            // 把买家相关订单放到session
            model.addAttribute("orderList", orderService.getOrderListByClient(loginUser.getId(), 0));

            if ("user".equals(loginUser.getResponsibility())) {
                model.addAttribute("loginUser", loginUser);
                return "redirect:/user/mainForm";
            } else if ("merchant".equals(loginUser.getResponsibility())) {
                model.addAttribute("businessLoginUser", loginUser);
                return "redirect:/user/merchantForm";
            } else if ("admin".equals(loginUser.getResponsibility())) {
                model.addAttribute("loginUser", loginUser);
                return "redirect:/user/ManagerForm";
            } else {
                model.addAttribute("loginMsg", loginUser.getResponsibility());
                return "redirect:/user/loginForm";
            }
        } else {
            throw new LoginException("LOGIN_FAILED", "账号或密码错误");
        }
    }

}
