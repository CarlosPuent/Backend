package com.usuarios.apiUsuarios.controller;

import com.usuarios.apiUsuarios.model.dto.DepartmentRequest;
import com.usuarios.apiUsuarios.model.dto.DepartmentResponse;
import com.usuarios.apiUsuarios.model.entity.DepartmentEntity;
import com.usuarios.apiUsuarios.service.its.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Department Management", description = "APIs for managing departments")
@RestController
@RequiredArgsConstructor
@RequestMapping("/departments")
@CrossOrigin(originPatterns = "*")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Operation(summary = "Get all departments", description = "Returns a list of all departments")
    @GetMapping
    public List<DepartmentResponse> list() {
        try {
            Thread.sleep(2000L);  // Añadir retraso de 2 milisegundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return departmentService.getAllDepartments();
    }

    @Operation(summary = "Get departments by page", description = "Returns a paginated list of departments")
    @GetMapping("/page/{page}")
    public Page<DepartmentResponse> list(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 4);
        return departmentService.findAll(pageable);
    }

    @Operation(summary = "Get department by ID", description = "Returns a department by its ID")
    @GetMapping("{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        try {
            DepartmentResponse department = departmentService.getDepartmentById(id)
                    .orElseThrow(() -> new RuntimeException("El departamento no existe"));
            return ResponseEntity.ok(department);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Create a new department", description = "Creates a new department")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody DepartmentEntity department, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.addDepartment(department));
    }

    @Operation(summary = "Update a department", description = "Updates an existing department")
    @PutMapping("{id}")
    public ResponseEntity<?> update(@Valid @RequestBody DepartmentRequest departmentRequest, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validation(result);
        }
        Optional<DepartmentResponse> departmentOptional = departmentService.update(departmentRequest, id);

        if (departmentOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(departmentOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete a department", description = "Deletes a department by its ID")
    @DeleteMapping("{id}")
    public ResponseEntity<?> remove(@PathVariable Long id) {
        Optional<DepartmentResponse> departmentOptional = departmentService.getDepartmentById(id);

        if (departmentOptional.isPresent()) {
            departmentService.deleteDepartment(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}