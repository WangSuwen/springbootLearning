package com.springbootLearning.dto;

import lombok.Data;

@Data
public class UserListDTO {
    private int id;
    private String name;
    private String email;

    public UserListDTO(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
