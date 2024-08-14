package com.springbootLearning.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "用户信息实体类")
public class Account {
    @Schema(description = "用户名")
    private String name;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "密码")
    private String password;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Account() {
        
    }
    public Account(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
