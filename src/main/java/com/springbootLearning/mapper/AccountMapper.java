package com.springbootLearning.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springbootLearning.entity.Account;



@Mapper
public interface AccountMapper extends BaseMapper<Account> {
    
}
