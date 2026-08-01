package com.springbootLearning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

/**
 *  1、mybatis-plus 不会自动创建表或者修改表结构，需要用 jpa 注解来创建；
 *  2、createTime 和 updateTime 字段需要用mybatis-plus 注解来自动填充；
 */

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @TableId(type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "email", length = 25)
    private String email;

    @Column(name = "password", length = 30, nullable = false)
    private String password;

    // 默认为当前时间
    @TableField(fill = FieldFill.INSERT, value = "create_time")
    private LocalDateTime createTime;

    // FieldFill.INSERT_UPDATE 注解，自动生成 updateTime 字段
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "update_time")
    private LocalDateTime updateTime;
}
