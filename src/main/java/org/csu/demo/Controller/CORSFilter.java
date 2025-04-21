package org.csu.demo.Controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@WebFilter(filterName = "CORSFilter")
@Slf4j
public class CORSFilter implements Filter {

    Logger logger = LoggerFactory.getLogger(CORSFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        logger.info("跨源允许已生效…………");

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        //允许谁跨域（谁请求我，我允许谁跨域，origin获得源）
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        //允许跨域请求时带上cookie
        response.setHeader("Access-Control-Allow-Credentials", "true");
        //允许哪些请求
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,PATCH,DELETE,PUT");
        //最长缓存时间
        response.setHeader("Access-Control-Max-Age", "3600");
        //超时时间等。。
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        //不要忘记
        filterChain.doFilter(request,response);

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
