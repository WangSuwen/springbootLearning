package com.springbootLearning.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class OrderController {
    
    @GetMapping("/orders")
    public String getOrders(@RequestParam String param) {
        return "这是订单列表";
    }
    
}
