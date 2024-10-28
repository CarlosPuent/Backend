package com.usuarios.apiUsuarios.service.its;

import com.usuarios.apiUsuarios.model.dto.DepartmentRequest;
import com.usuarios.apiUsuarios.model.dto.DepartmentResponse;
import com.usuarios.apiUsuarios.model.entity.DepartmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    List<DepartmentResponse> getAllDepartments();
    Page<DepartmentResponse> findAll(Pageable pageable);
    Optional<DepartmentResponse> getDepartmentById(Long id);
    DepartmentResponse addDepartment(DepartmentEntity department);
    Optional<DepartmentResponse> update(DepartmentRequest departmentRequest, Long id);
    void deleteDepartment(Long id);

    Optional<DepartmentResponse> findDepartmentByName(String name);
}

