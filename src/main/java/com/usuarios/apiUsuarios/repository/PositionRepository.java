package com.usuarios.apiUsuarios.repository;

import com.usuarios.apiUsuarios.model.entity.PositionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<PositionEntity, Long> {

    @Query("SELECT r FROM PositionEntity r WHERE r.name=?1")
    Optional<PositionEntity> findByName(String name);

    Page<PositionEntity> findAll(Pageable pageable);

    boolean existsByName(String name);
}
