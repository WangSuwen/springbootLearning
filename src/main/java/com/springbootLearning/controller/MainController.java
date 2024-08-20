package com.springbootLearning.controller;

import com.springbootLearning.dto.UserAddDTO;
import com.springbootLearning.entity.User;
import com.springbootLearning.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.springbootLearning.entity.Account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;


@Controller
@RequestMapping("/api/user")
@Tag(name = "用户登录相关接口", description = "注册、登录、登出、修改密码、获取用户信息、发送短信等接口")
public class MainController {

    @Autowired
    UserMapper userMapper;

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

    /**
     * 添加用户，使用 application/json 方式传参，并校验某一个参数的必填性
     * @param param
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/add", consumes = "application/json")
    public Object addUser(
            @Valid @RequestBody UserAddDTO param
    ) {
        User user = new User();
        user.setName(param.getName());
        user.setEmail(param.getEmail());
        user.setPassword(param.getPassword());
        int uc = userMapper.insert(user);
        if (uc == 1) {
            return user;
        } else {
            return null;
        }
    }
}
