package com.springbootLearning.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 构建 post 请求参数，并校验参数
 */
@Data
public class UserAddDTO {
    @NotNull(message = "请填写姓名")
    private String name;
    @NotNull(message = "请填写邮箱")
    private String email;
    @NotNull(message = "请填写密码")
    private String password;
}
