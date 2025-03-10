package org.csu.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Bean
    public MethodValidationPostProcessor viewResolver() {
        return new MethodValidationPostProcessor();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("resources/","static/", "public/", "META_INF/resources/")
                .addResourceLocations("classpath:resources/", "classpath:static/", "classpath:public/", "classpath:META-INF/resources/")
                .addResourceLocations("file:///tmp/webapps/");
    }
}
