package com.usuarios.apiUsuarios.mapper;

import com.usuarios.apiUsuarios.model.dto.DepartmentResponse;
import com.usuarios.apiUsuarios.model.entity.DepartmentEntity;
import lombok.Setter;

@Setter
public class DtoMapperDepartment {

    private DepartmentEntity department;

    public DtoMapperDepartment() {}

    public static DtoMapperDepartment builder() {
        return new DtoMapperDepartment();
    }

    public DtoMapperDepartment setDepartment(DepartmentEntity department) {  // Cambio de nombre
        this.department = department;
        return this;
    }

    public DepartmentResponse build() {
        if (department == null) {
            throw new RuntimeException("must pass the entity department!");
        }
        return new DepartmentResponse(this.department.getId(), department.getName());
    }
}
