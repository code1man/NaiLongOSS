package org.csu.demo.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.csu.demo.domain.User;
import org.csu.demo.persistence.UserDao;
import org.csu.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserDao userDao;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User user = authToken.getPrincipal();

        User oauthUser = new User();
        oauthUser.setUsername(user.getAttribute("login"));
        oauthUser.setPassword("OAUTH_USER");
        // 从用户信息中提取需要的数据，比如用户名
        boolean isExist = userDao.findByUsername(oauthUser.getUsername())!= null;
        if(!isExist) userDao.addUser(oauthUser);         // 若没使用 OAuth2 登录过，则加入到数据库中
        // 将用户信息存入 session
        request.getSession().setAttribute("loginUser", oauthUser);

        // 重定向到首页或其他页面
        response.sendRedirect("/mainForm");
    }
}