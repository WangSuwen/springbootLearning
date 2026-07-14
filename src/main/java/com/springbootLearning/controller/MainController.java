package com.springbootLearning.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springbootLearning.dto.UserAddDTO;
import com.springbootLearning.entity.User;
import com.springbootLearning.mapper.UserMapper;
import com.springbootLearning.service.UserService;
import com.springbootLearning.utils.ResultEnum;
import com.springbootLearning.utils.ResultResponse;
import com.springbootLearning.vo.UserListVO;


import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.springbootLearning.entity.Account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import com.springbootLearning.annotation.LogRequired;

@RestController
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
    @PostMapping(value = "/add", consumes = "application/json")
    public ResultResponse<?> addUser(
            @Valid @RequestBody UserAddDTO param
    ) {
        User user = new User();
        user.setName(param.getName());
        user.setEmail(param.getEmail());
        user.setPassword(param.getPassword());
        int uc = userMapper.insert(user);
        if (uc == 1) {
            UserListVO userListVO = new UserListVO();
            BeanUtils.copyProperties(user, userListVO);
            return ResultResponse.success(userListVO);
        } else {
            return ResultResponse.success(null);
        }
    }

    @GetMapping("/get")
    public ResultResponse<?> get(
            @RequestParam String name
    ) {
        if (name == null || name.trim().isEmpty()) {
            return ResultResponse.failed(ResultEnum.PARAMS_ERROR.valueOf(), ResultEnum.PARAMS_ERROR.getMsg(), "请输入姓名");
        }
        QueryWrapper<User> qw = new QueryWrapper<>();
        User user = userMapper.selectOne(qw.eq("name", name));
        return  ResultResponse.success(user);
    }

    @LogRequired(description = "查询用户列表")
    @GetMapping("/list")
    public ResultResponse<?> getList(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String name
    ) {
        Page<User> userPage = userMapper.selectPage(
                Page.of(current, size),
                Wrappers.<User>query()
                        .eq((name != null && !"".equals(name)), "name", name)
                        .orderByDesc("id")
                        .select("id", "name", "email", "password", "create_time", "update_time")
        );
        // 通过VO类，构建只需要返给前端的字段，屏蔽掉User类中其他字段名，不需要将所有字段都返给前端（返给前端时，这些不需要的字段的值都是null）
        return getResultResponse(userPage);
    }

//    TODO: 通过继承自 IService 接口的 service 查询数据

    @GetMapping("/list-service")
    public ResultResponse<?> getListByService (
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String name
    ) {
        Page<User> userPage = userService.page(
                Page.of(current, size),
                Wrappers.<User>query()
                        .eq((name != null && !"".equals(name)), "name", name)
                        .orderByDesc("id")
                        .select("id", "name", "email")
        );

        return getResultResponse(userPage);
    }

    @SuppressWarnings("null")
    private ResultResponse<?> getResultResponse(Page<User> userPage) {
        // 构建返回的Page对象
        /* Page<UserListVO> userListVOPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserListVO> userListVOs = userPage.getRecords().stream().map(user -> {
            UserListVO userListVO = new UserListVO();
            BeanUtils.copyProperties(user, userListVO);
            userListVO.setCreateTime(Objects.isNull(user.getCreateTime()) ? null : user.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            return userListVO;
        }).toList();
        userListVOPage.setRecords(userListVOs);
        return ResultResponse.success(userListVOPage); */
        
        // 构建返回的JSONObject
        List<UserListVO> userListVOs = userPage.getRecords().stream().map(user -> {
            UserListVO userListVO = new UserListVO();
            BeanUtils.copyProperties(user, userListVO);
            userListVO.setCreateTime(Objects.isNull(user.getCreateTime()) ? null : user.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            return userListVO;
        }).toList();
        JSONObject jsonObject = JSONObject.of(
            "total", userPage.getTotal(),
            "size", userPage.getSize(),
            "current", userPage.getCurrent(),
            "pages", userPage.getPages(),
            "records", userListVOs
        );
        return ResultResponse.success(jsonObject);
    }


}
