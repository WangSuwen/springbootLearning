package com.springbootLearning.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Data;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;



/**
 *  1、mybatis-plus 不会自动创建表或者修改表结构，需要用 jpa 注解来创建；
 *  2、createTime 和 updateTime 字段需要用mybatis-plus 注解来自动填充；
 */

@Schema(description = "账户信息实体类")
@Data
@Entity
@Table(name = "account")
public class Account {

    @Id
    @TableId(type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "用户名")
    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Schema(description = "邮箱")
    @Column(name = "email", length = 25)
    private String email;

    @Schema(description = "密码")
    @Column(name = "password", length = 30, nullable = false)
    private String password;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT, value = "create_time")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "update_time")
    private LocalDateTime updateTime;

}
