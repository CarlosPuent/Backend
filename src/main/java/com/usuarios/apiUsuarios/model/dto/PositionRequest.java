package com.usuarios.apiUsuarios.model.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PositionRequest {

    @NotBlank(message = "Position name cannot be null or empty")
    private String name;
}