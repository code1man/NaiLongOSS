package org.csu.demo.interceptor;

import jakarta.servlet.http.HttpSession;
import org.csu.demo.domain.Category;
import org.csu.demo.domain.Order;
import org.csu.demo.domain.OrderItem;
import org.csu.demo.domain.Product;
import org.csu.demo.service.CatalogService;
import org.csu.demo.service.OrderService;
import org.csu.demo.service.ProductService;
import org.csu.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class CategoryInterceptor implements HandlerInterceptor {

    @Autowired
    private ProductService productService;

    @Autowired
    private CatalogService catalogService;
    @Autowired
    private OrderService orderService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true; // 继续执行
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        if (modelAndView != null) {
            List<Category> categoryList = catalogService.getCategories();
            List<Product> productList = productService.getProducts();
            List<OrderItem> orderList = orderService.getAllOrders();

            modelAndView.addObject("categoryList", categoryList);
            modelAndView.addObject("productList", productList);
            modelAndView.addObject("orderList", orderList);
        }
    }


}
