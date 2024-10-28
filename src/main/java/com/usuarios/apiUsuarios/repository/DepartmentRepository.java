package com.usuarios.apiUsuarios.repository;

import com.usuarios.apiUsuarios.model.entity.DepartmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

    @Query("SELECT d FROM DepartmentEntity d WHERE d.name=?1")
    Optional<DepartmentEntity> findByName(String name);

    Page<DepartmentEntity> findAll(Pageable pageable);

    boolean existsByName(String name);
}
