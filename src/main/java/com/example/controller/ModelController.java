package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @version V1.0
 * @author: hezheng
 * @date: 2020/4/1 12:04
 */
@Controller
public class ModelController {
    @RequestMapping("/")
    public String toWelcome() {
        return "welcome";
    }
}
