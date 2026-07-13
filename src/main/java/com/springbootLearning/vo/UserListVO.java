package com.springbootLearning.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Data 注解，自动生成 get 方法
// @NoArgsConstructor 注解，自动生成无参构造方法
// @AllArgsConstructor 注解，自动生成有参构造方法
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListVO {
    private Long id;
    private String name;
    private String email;
    private String createTime;
}
