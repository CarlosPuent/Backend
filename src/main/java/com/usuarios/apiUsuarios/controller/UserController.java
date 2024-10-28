package com.usuarios.apiUsuarios.controller;

import com.usuarios.apiUsuarios.model.dto.UserRequest;
import com.usuarios.apiUsuarios.model.dto.UserResponse;
import com.usuarios.apiUsuarios.model.entity.UserEntity;
import com.usuarios.apiUsuarios.service.its.UserService;
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

@Tag(name = "User Management", description = "APIs for managing users")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin(originPatterns = "*")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get all users", description = "Returns a list of all users")
    @GetMapping
    public List<UserResponse> list() {
        // Darle 2 milisegundos de retraso
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userService.findAll();
    }

    @Operation(summary = "Get users by page", description = "Returns a paginated list of users")
    @GetMapping("/page/{page}")
    public Page<UserResponse> list(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 6);
        return userService.findAll(pageable);
    }

    @Operation(summary = "Get user by ID", description = "Returns a user by its ID")
    @GetMapping("{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        try {
            UserResponse user = userService.findById(id)
                    .orElseThrow(() -> new RuntimeException("El usuario buscado no existe"));
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Create a new user", description = "Creates a new user")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserRequest user, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @Operation(summary = "Update a user", description = "Updates an existing user")
    @PutMapping("{id}")
    public ResponseEntity<?> update(@Valid @RequestBody UserRequest user, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validation(result);
        }
        Optional<UserResponse> userOptional = userService.update(user, id);

        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete a user", description = "Deletes a user by its ID")
    @DeleteMapping("{id}")
    public ResponseEntity<?> remove(@PathVariable Long id) {
        Optional<UserResponse> userOptional = userService.findById(id);

        if (userOptional.isPresent()) {
            userService.deleteById(id);
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), " El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}