package com.usuarios.apiUsuarios.controller;

import com.usuarios.apiUsuarios.model.dto.PositionRequest;
import com.usuarios.apiUsuarios.model.dto.PositionResponse;
import com.usuarios.apiUsuarios.model.entity.PositionEntity;
import com.usuarios.apiUsuarios.service.its.PositionService;
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

@Tag(name = "Position Management", description = "APIs for managing positions")
@RestController
@RequiredArgsConstructor
@RequestMapping("/positions")
@CrossOrigin(originPatterns = "*")
public class PositionController {

    private final PositionService positionService;

    @Operation(summary = "Get all positions", description = "Returns a list of all positions")
    @GetMapping
    public List<PositionResponse> list() {
        try {
            Thread.sleep(2000L);  // Añadir retraso de 2 milisegundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return positionService.getAllPositions();
    }

    @Operation(summary = "Get positions by page", description = "Returns a paginated list of positions")
    @GetMapping("/page/{page}")
    public Page<PositionResponse> list(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 4);
        return positionService.findAll(pageable);
    }

    @Operation(summary = "Get position by ID", description = "Returns a position by its ID")
    @GetMapping("{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        try {
            PositionResponse position = positionService.getPositionById(id)
                    .orElseThrow(() -> new RuntimeException("La posición no existe"));
            return ResponseEntity.ok(position);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Create a new position", description = "Creates a new position")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody PositionEntity position, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(positionService.addPosition(position));
    }

    @Operation(summary = "Update a position", description = "Updates an existing position")
    @PutMapping("{id}")
    public ResponseEntity<?> update(@Valid @RequestBody PositionRequest positionRequest, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validation(result);
        }
        Optional<PositionResponse> positionOptional = positionService.updatePosition(id, positionRequest);

        if (positionOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(positionOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete a position", description = "Deletes a position by its ID")
    @DeleteMapping("{id}")
    public ResponseEntity<?> remove(@PathVariable Long id) {
        Optional<PositionResponse> positionOptional = positionService.getPositionById(id);

        if (positionOptional.isPresent()) {
            positionService.deletePosition(id);
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