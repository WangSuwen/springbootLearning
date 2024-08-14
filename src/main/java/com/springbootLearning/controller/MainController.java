package com.springbootLearning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springbootLearning.entity.Account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@Tag(name = "用户登录相关接口", description = "注册、登录、登出、修改密码、获取用户信息、发送短信等接口")
public class MainController {
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "成功"),
        @ApiResponse(responseCode = "500", description = "失败")
    })
    @Operation(summary = "用户登录", description = "用户登录接口")
    @ResponseBody
    @PostMapping("/submit")
    public Account submit(
        @Parameter(description = "用户名", required = true) @RequestParam String name,
        @Parameter(description = "密码") @RequestParam(required = false) String password
    ) {
        
        return new Account();
    }
    
}
