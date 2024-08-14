package com.springbootLearning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class MainController {
    @ResponseBody
    @PostMapping("/submit")
    public String submit(String name, String password) {
        
        return "登录成功";
    }
    
}
