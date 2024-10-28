package com.usuarios.apiUsuarios.service.its;

import com.usuarios.apiUsuarios.model.dto.UserRequest;
import com.usuarios.apiUsuarios.model.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserResponse> findAll();

    Page<UserResponse> findAll(Pageable pageable);

    Optional<UserResponse> findById(Long id);

    UserResponse save(UserRequest userRequest); // Modificado para aceptar UserRequest

    Optional<UserResponse> update(UserRequest userRequest, Long id); // Modificado para aceptar UserRequest

    void deleteById(Long id);
}
