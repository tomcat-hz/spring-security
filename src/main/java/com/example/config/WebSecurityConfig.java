package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * @version V1.0
 * @author: hezheng
 * @date: 2020/4/1 12:26
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                //设置用户名
                .withUser("admin")
                //设置密码
                .password("123")
                //设置角色
                .roles("ADMIN", "USER")
                //多个用户用and连接
                .and()
                .withUser("user")
                .password("123")
                .roles("USER")
                .and()
                .withUser("dba")
                .password("123")
                .roles("DBA");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       //开启HttpSecurity的配置
        //antfengge
        http.authorizeRequests()
                //匹配路径以/admin/开头的url资源
                .antMatchers("/admin/**")
                //具备ADMIN
                .hasRole("ADMIN")
                //匹配路径为/user/开头的url资源
                .antMatchers("/user/**")
                //具备ADMIN或者USER角色  或
                .access("hasAnyRole('ADMIN','USER')")
                //匹配路径以/db/开头的url资源
                .antMatchers("/db/**")
                //同时具备ADMIN和DBA角色
                .access("hasRole('ADMIN') and hasRole('DBA')")
                //剩下的资源
                .anyRequest()
                //必须登录后访问
                .authenticated()
                .and()
                //开启表单验证
                .formLogin()
                //设置自定义登录页面
                .loginPage("/login_page")
                .usernameParameter("uname")
                .passwordParameter("pwd")
                //登录的url为/doLogin
                .loginProcessingUrl("/doLogin")
                //处理登录成功的逻辑
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
                        //设置json响应
                        response.setContentType("application/json:charset=UTF-8");
                        Object principal = auth.getPrincipal();
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("msg", principal);
                        map.put("status", 200);
                        PrintWriter out = response.getWriter();
                        out.println(new ObjectMapper().writeValueAsString(map));
                        out.flush();
                        out.close();
                    }
                })
                //处理失败的逻辑
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        response.setContentType("application/json;charset=UTF-8");
                        PrintWriter out = response.getWriter();
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("status", 401);
                        if (e instanceof LockedException) {
                            map.put("msg", "账号被锁定,请联系管理员");
                        } else if (e instanceof DisabledException) {
                            map.put("msg", "账号被禁用,请联系管理员");
                        } else if (e instanceof CredentialsExpiredException) {
                            map.put("msg", "密码过期,请联系管理员");
                        } else if (e instanceof AccountExpiredException) {
                            map.put("msg", "账号过期,请联系管理员");
                        } else if (e instanceof BadCredentialsException) {
                            map.put("msg", "用户名或者密码错误");
                        } else {
                            map.put("msg", "未知原因,登录失败");
                        }
                        out.write(new ObjectMapper().writeValueAsString(map));
                        out.flush();
                        out.close();
                    }
                })
                //开启登录的接口不需要认证
                .permitAll()
                .and()
                //关闭csrf,否则会认为是跨站攻击导致无法测试
                .csrf()
                .disable();

    }




}
