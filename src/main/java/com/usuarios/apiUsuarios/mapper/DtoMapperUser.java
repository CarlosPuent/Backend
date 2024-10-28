package com.usuarios.apiUsuarios.mapper;

import com.usuarios.apiUsuarios.model.dto.UserResponse;
import com.usuarios.apiUsuarios.model.entity.UserEntity;

public class DtoMapperUser {

    public static UserResponse map(UserEntity user) {
        if (user == null) {
            throw new RuntimeException("must pass the entity user!");
        }

        // Verificar si el usuario tiene el rol de admin
        boolean isAdmin = user.getRoles().stream()
                .anyMatch(r -> "ROLE_ADMIN".equals(r.getName()));

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                isAdmin,
                user.getDepartment().getId(),
                user.getDepartment().getName(),
                user.getPosition().getId(),
                user.getPosition().getName()
        );
    }
}
