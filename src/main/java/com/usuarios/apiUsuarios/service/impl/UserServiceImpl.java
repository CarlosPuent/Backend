package com.usuarios.apiUsuarios.service.impl;

import com.usuarios.apiUsuarios.mapper.DtoMapperUser;
import com.usuarios.apiUsuarios.model.IUser;
import com.usuarios.apiUsuarios.model.dto.UserRequest;
import com.usuarios.apiUsuarios.model.dto.UserResponse;
import com.usuarios.apiUsuarios.model.entity.DepartmentEntity;
import com.usuarios.apiUsuarios.model.entity.PositionEntity;
import com.usuarios.apiUsuarios.model.entity.RoleEntity;
import com.usuarios.apiUsuarios.model.entity.UserEntity;
import com.usuarios.apiUsuarios.repository.DepartmentRepository;
import com.usuarios.apiUsuarios.repository.PositionRepository;
import com.usuarios.apiUsuarios.repository.RoleRepository;
import com.usuarios.apiUsuarios.repository.UserRespository;
import com.usuarios.apiUsuarios.service.its.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRespository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        List<UserEntity> users = repository.findAll();
        return users.stream()
                .map(DtoMapperUser::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> findAll(Pageable pageable) {
        Page<UserEntity> usersPage = repository.findAll(pageable);
        return usersPage.map(DtoMapperUser::map);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponse> findById(Long id) {
        return repository.findById(id)
                .map(DtoMapperUser::map);
    }

    @Override
    @Transactional
    public UserResponse save(UserRequest userRequest) {
        if (repository.existsByUsername(userRequest.getUsername())) {
            throw new RuntimeException("El usuario ya existe");
        }

        // Crear un nuevo UserEntity
        UserEntity user = new UserEntity();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        // Asignar departamento y posición
        setDepartmentAndPosition(userRequest, user);

        // Asignar roles
        user.setRoles(getRoles(user));

        // Guardar y mapear la respuesta
        return DtoMapperUser.map(repository.save(user));
    }

    @Override
    @Transactional
    public Optional<UserResponse> update(UserRequest userRequest, Long id) {
        return repository.findById(id)
                .map(userEntity -> {
                    // Actualizar campos del usuario
                    userEntity.setUsername(userRequest.getUsername());
                    userEntity.setEmail(userRequest.getEmail());

                    // Asignar departamento y posición
                    setDepartmentAndPosition(userRequest, userEntity);

                    // Asignar roles
                    userEntity.setRoles(getRoles(userRequest));

                    // Guardar y mapear la respuesta
                    return DtoMapperUser.map(repository.save(userEntity));
                });
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    // Método para asignar roles
    private List<RoleEntity> getRoles(IUser user) {
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found")));

        // Si el usuario es administrador, agregar ROLE_ADMIN
        if (user.isAdmin()) {
            roles.add(roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found")));
        }
        return roles;
    }

    private void setDepartmentAndPosition(UserRequest request, UserEntity user) {
        DepartmentEntity department = findDepartment(request.getDepartmentId(), request.getDepartmentName());
        PositionEntity position = findPosition(request.getPositionId(), request.getPositionName());

        user.setDepartment(department);
        user.setPosition(position);
    }

    // Método para buscar departamento por id o nombre
    private DepartmentEntity findDepartment(Long departmentId, String departmentName) {
        if (departmentId != null) {
            return departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("El departamento con el ID proporcionado no existe"));
        } else if (departmentName != null) {
            return departmentRepository.findByName(departmentName)
                    .orElseThrow(() -> new RuntimeException("El nombre del departamento proporcionado no existe"));
        } else {
            throw new RuntimeException("Debe proporcionar departmentId o departmentName");
        }
    }

    // Método para buscar posición por id o nombre
    private PositionEntity findPosition(Long positionId, String positionName) {
        if (positionId != null) {
            return positionRepository.findById(positionId)
                    .orElseThrow(() -> new RuntimeException("La posición con el ID proporcionado no existe"));
        } else if (positionName != null) {
            return positionRepository.findByName(positionName)
                    .orElseThrow(() -> new RuntimeException("El nombre de la posición proporcionado no existe"));
        } else {
            throw new RuntimeException("Debe proporcionar positionId o positionName");
        }
    }
}
