package com.usuarios.apiUsuarios.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private boolean admin;

    private Long departmentId;
    private String departmentName;

    private Long positionId;
    private String positionName;
}