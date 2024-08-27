package com.springbootLearning.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootLearning.dto.UserAddDTO;
import com.springbootLearning.dto.UserListDTO;
import com.springbootLearning.entity.User;
import com.springbootLearning.mapper.UserMapper;
import com.springbootLearning.service.UserService;
import com.springbootLearning.utils.ResultResponse;
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

import java.util.List;

@Controller
@RequestMapping("/api/user")
@Tag(name = "用户登录相关接口", description = "注册、登录、登出、修改密码、获取用户信息、发送短信等接口")
public class MainController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;

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
     */
    @ResponseBody
    @PostMapping(value = "/add", consumes = "application/json")
    public ResultResponse addUser(
            @Valid @RequestBody UserAddDTO param
    ) {
        User user = new User();
        user.setName(param.getName());
        user.setEmail(param.getEmail());
        user.setPassword(param.getPassword());
        int uc = userMapper.insert(user);
        if (uc == 1) {
            return ResultResponse.success(user);
        } else {
            return ResultResponse.success(null);
        }
    }

    @ResponseBody
    @GetMapping("/get")
    public ResultResponse get(
            @RequestParam String name
    ) {
        if (name == null || name.trim().isEmpty()) {
            return ResultResponse.failed(ResultResponse.PARAMS_ERROR, ResultResponse.PARAMS_ERROR_MSG, "请输入姓名");
        }
        QueryWrapper<User> qw = new QueryWrapper<>();
        User user = userMapper.selectOne(qw.eq("name", name));
        return  ResultResponse.success(user);
    }

    @ResponseBody
    @GetMapping("/list")
    public ResultResponse getList(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String name
    ) {
        Page<User> userPage = userMapper.selectPage(
                Page.of(current, size),
                Wrappers.<User>query()
                        .eq((name != null && !name.equals("")), "name", name)
                        .orderByDesc("id")
                        .select("id", "name", "email")
        );
        // TODO: 通过DTO类，构建只需要返给前端的字段，屏蔽掉User类中其他字段名，不需要将所有字段都返给前端（返给前端时，这些不需要的字段的值都是null）
        return getResultResponse(userPage);
    }

//    TODO: 通过继承自 IService 接口的 service 查询数据
    @ResponseBody
    @GetMapping("/list-service")
    public ResultResponse getListByService (
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String name
    ) {
        Page<User> userPage = userService.page(
                Page.of(current, size),
                Wrappers.<User>query()
                        .eq((name != null && !name.equals("")), "name", name)
                        .orderByDesc("id")
                        .select("id", "name", "email")
        );

        return getResultResponse(userPage);
    }

    private ResultResponse getResultResponse(Page<User> userPage) {
        Page<UserListDTO> userListDTOPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserListDTO> userListDTOS = userPage.getRecords().stream().map(user -> {
            return new UserListDTO(user.getId(), user.getName(), user.getEmail());
        }).toList();
        userListDTOPage.setRecords(userListDTOS);
        return ResultResponse.success(userListDTOPage);
    }


}
