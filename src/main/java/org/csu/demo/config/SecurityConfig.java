package org.csu.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**","/", "/images/**", "/login/oauth2/**", "/oauth2/**", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
//                .formLogin(form -> form
//                        .loginPage("/loginForm")
//                        .loginProcessingUrl("/login")     // 修改 Spring Security 默认的登录处理 URL(login)
//                        .defaultSuccessUrl("/main", true)
//                        .permitAll()
//                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/loginForm")
                        .defaultSuccessUrl("/main", true)  // 认证成功后跳转到 /main
                )
                .logout(LogoutConfigurer::permitAll
                );
        return http.build();
    }


}
