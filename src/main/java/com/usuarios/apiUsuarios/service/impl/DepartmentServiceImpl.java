package com.usuarios.apiUsuarios.service.impl;

import com.usuarios.apiUsuarios.mapper.DtoMapperDepartment;
import com.usuarios.apiUsuarios.model.dto.DepartmentRequest;
import com.usuarios.apiUsuarios.model.dto.DepartmentResponse;
import com.usuarios.apiUsuarios.model.entity.DepartmentEntity;
import com.usuarios.apiUsuarios.repository.DepartmentRepository;
import com.usuarios.apiUsuarios.service.its.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {
        List<DepartmentEntity> departments = departmentRepository.findAll();

        return departments
                .stream()
                .map(d -> DtoMapperDepartment.builder().setDepartment(d).build())  // Mejor nomenclatura
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentResponse> findAll(Pageable pageable) {
        Page<DepartmentEntity> departmentPage = departmentRepository.findAll(pageable);
        return departmentPage.map(d -> DtoMapperDepartment.builder().setDepartment(d).build()); // Mejor nomenclatura
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepartmentResponse> getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .map(d -> DtoMapperDepartment.builder().setDepartment(d).build()); // Mejor nomenclatura
    }

    @Override
    public DepartmentResponse addDepartment(DepartmentEntity department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new RuntimeException("El departamento ya existe");
        }
        return DtoMapperDepartment.builder()
                .setDepartment(departmentRepository.save(department))  // Mejor nomenclatura
                .build();
    }

    @Override
    public Optional<DepartmentResponse> update(DepartmentRequest departmentRequest, Long id) {
        Optional<DepartmentEntity> departmentOptional = departmentRepository.findById(id);
        if (!departmentOptional.isPresent()) {
            throw new RuntimeException("El departamento no fue encontrado");
        }

        DepartmentEntity departmentEntity = departmentOptional.orElseThrow();
        departmentEntity.setName(departmentRequest.getName());
        DepartmentEntity updatedDepartment = departmentRepository.save(departmentEntity);

        return Optional.of(DtoMapperDepartment.builder().setDepartment(updatedDepartment).build());  // Mejor nomenclatura
    }

    @Override
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("El departamento no fue encontrado para eliminar");
        }
        departmentRepository.deleteById(id);
    }

    @Override
    public Optional<DepartmentResponse> findDepartmentByName(String name) {
        return departmentRepository.findByName(name)
                .map(departmentEntity -> DtoMapperDepartment.builder()
                        .setDepartment(departmentEntity)
                        .build());
    }
}
