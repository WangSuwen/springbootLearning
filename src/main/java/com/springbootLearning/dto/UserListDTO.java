package com.springbootLearning.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListDTO {
    private Long id;
    private String name;
    private String email;

}
