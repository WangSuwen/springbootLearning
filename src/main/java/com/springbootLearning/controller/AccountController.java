package com.springbootLearning.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.time.format.DateTimeFormatter;
import java.util.List;


import com.springbootLearning.dto.AccountAddDTO;
import com.springbootLearning.entity.Account;
import com.springbootLearning.mapper.AccountMapper;
import com.springbootLearning.utils.ResultResponse;
import com.springbootLearning.vo.AccountVO;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountMapper accountMapper;

    // 添加账户
    @PostMapping("/add")
    public ResultResponse<?> addAccount(
        @Valid @RequestBody AccountAddDTO accountAddDTO
    ) {
        Account account = new Account();
        account.setName(accountAddDTO.getName());
        account.setEmail(accountAddDTO.getEmail());
        account.setPassword(accountAddDTO.getPassword());
        int result = accountMapper.insert(account);
        if (result == 1) {
            AccountVO accountVO = new AccountVO();
            BeanUtils.copyProperties(account, accountVO);
            accountVO.setId(account.getId());
            accountVO.setCreateTime(account.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            return ResultResponse.success(accountVO);
        } else {
            return ResultResponse.failed("添加账户失败");
        }
    }

    // 获取账户列表
    @GetMapping("/list")
    public ResultResponse<?> getAccountList(
        @RequestParam(required = false) String name,
        @RequestParam(defaultValue = "1") String current,
        @RequestParam(defaultValue = "10") String size
    ) {
        // 分页参数转换为 Integer 类型
        int currentPage = Integer.parseInt(current);
        int pageSize = Integer.parseInt(size);

        // 构建查询条件
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(Account::getName, name);
        }

        // 分页查询
        IPage<Account> accountPage = accountMapper.selectPage(
            new Page<>(currentPage, pageSize),
            queryWrapper.select(true, Account::getId, Account::getName, Account::getEmail)
        );

        return ResultResponse.success(accountPage);
    }
}
