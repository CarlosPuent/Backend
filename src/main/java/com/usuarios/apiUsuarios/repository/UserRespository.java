package com.usuarios.apiUsuarios.repository;

import com.usuarios.apiUsuarios.model.entity.RoleEntity;
import com.usuarios.apiUsuarios.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface UserRespository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE u.username=?1")
    Optional<UserEntity> findByUsername(String username);

    Page<UserEntity> findAll(Pageable pageable);

    @Query("SELECT r FROM RoleEntity r WHERE r.name=?1")
    Optional<RoleEntity> findByName(String name);

    boolean existsByUsername(String username);

}
