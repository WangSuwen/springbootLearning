package com.springbootLearning.controller;

import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class OrderController {
    

    @GetMapping("/orders")
    public Map getOrders(
        @RequestParam(required = false) String id
    ) {
        return new HashMap();
    }
    
}
