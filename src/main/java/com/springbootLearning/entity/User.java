package com.springbootLearning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;

/**
 * 这个实例对象是用 MyBatis-Plus 创建的
 */

@Data
@Entity
@Table(name = "user")
@TableName("user")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    Long id;

    @TableField("name")
    String name;

    @TableField("email")
    String email;

    @TableField("password")
    String password;

    // 默认为当前时间
    @TableField(fill = FieldFill.INSERT, value = "create_time")
    LocalDateTime createTime;

    // FieldFill.INSERT_UPDATE 注解，自动生成 updateTime 字段
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "update_time")
    LocalDateTime updateTime;
}
