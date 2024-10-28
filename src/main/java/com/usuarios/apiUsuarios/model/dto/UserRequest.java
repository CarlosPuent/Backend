package com.usuarios.apiUsuarios.model.dto;

import com.usuarios.apiUsuarios.model.IUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest implements IUser {

    @NotBlank(message = "Username cannot be null or empty")
    private String username;

    @NotBlank(message = "Email cannot be null or empty")
    @Email(message = "Email should be valid")
    private String email;

    private String password;

    private boolean admin;

    //@NotNull(message = "Department ID cannot be null")
    private Long departmentId;

    //@NotBlank(message = "Department Name cannot be black")
    private String departmentName;

    //@NotNull(message = "Position ID cannot be null")
    private Long positionId;

    //@NotBlank(message = "Position Name cannot be black")
    private String positionName;
}


