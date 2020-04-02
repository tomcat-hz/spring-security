package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @version V1.0
 * @author: hezheng
 * @date: 2020/4/1 12:50
 */
@Controller
public class LoginController {
    @GetMapping("login_page")
    public String login_page() {
        return "login_page";
    }
}
