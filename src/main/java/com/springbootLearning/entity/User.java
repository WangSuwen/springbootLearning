package com.springbootLearning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.Data;

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
    int id;

    @TableField("name")
    @Column(name = "name")
    String name;

    @Column(name = "email")
    @TableField("email")
    String email;

    @Column(name = "password")
    @TableField("password")
    String password;
}
