package com.springbootLearning.vo;

import lombok.Data;

@Data
public class AccountVO {
    /**
     * 账户ID
     */
    private Long id;
    /**
     * 账户名称
     */
    private String name;
    /**
     * 账户邮箱
     */
    private String email;
    /**
     * 创建时间
     */
    private String createTime;
}
