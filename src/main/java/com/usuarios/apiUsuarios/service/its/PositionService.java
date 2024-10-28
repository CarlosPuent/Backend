package com.usuarios.apiUsuarios.service.its;

import com.usuarios.apiUsuarios.model.dto.PositionRequest;
import com.usuarios.apiUsuarios.model.dto.PositionResponse;
import com.usuarios.apiUsuarios.model.entity.PositionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface PositionService {

    List<PositionResponse> getAllPositions();
    Page<PositionResponse> findAll(Pageable pageable);
    Optional<PositionResponse> getPositionById(Long id);
    PositionResponse addPosition(PositionEntity position);
    Optional<PositionResponse> updatePosition(Long id, PositionRequest position);
    void deletePosition(Long id);

    Optional<PositionResponse> findPositionByName(String name);
}

