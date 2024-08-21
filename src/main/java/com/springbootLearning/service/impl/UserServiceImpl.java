package com.springbootLearning.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootLearning.entity.User;
import com.springbootLearning.mapper.UserMapper;
import com.springbootLearning.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
