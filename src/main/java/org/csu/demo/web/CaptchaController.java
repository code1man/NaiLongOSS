package org.csu.demo.web;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.csu.demo.persistence.Util.CaptchaUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CaptchaController {
    // 图片验证码的核心逻辑就是理解image中的src请求实际上就是一次get

    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response, HttpSession session) {
        // 调用之前写的 CaptchaUtil 类生成验证码图片并返回验证码文本
        String captchaText = CaptchaUtil.createCaptchaImage(response);
        // 将验证码文本存入 session，用于后续验证
        session.setAttribute("captcha", captchaText);
    }

}
