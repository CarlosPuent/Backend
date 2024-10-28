package com.usuarios.apiUsuarios.service.impl;

import com.usuarios.apiUsuarios.mapper.DtoMapperPosition;
import com.usuarios.apiUsuarios.model.dto.PositionRequest;
import com.usuarios.apiUsuarios.model.dto.PositionResponse;
import com.usuarios.apiUsuarios.model.entity.PositionEntity;
import com.usuarios.apiUsuarios.repository.PositionRepository;
import com.usuarios.apiUsuarios.service.its.PositionService;
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
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PositionResponse> getAllPositions() {
        List<PositionEntity> positions = positionRepository.findAll();

        return positions
                .stream()
                .map(p -> DtoMapperPosition.builder().setPosition(p).build())  // Mejor nomenclatura
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PositionResponse> findAll(Pageable pageable) {
        Page<PositionEntity> positionPage = positionRepository.findAll(pageable);
        return positionPage.map(p -> DtoMapperPosition.builder().setPosition(p).build());  // Mejor nomenclatura
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PositionResponse> getPositionById(Long id) {
        return positionRepository.findById(id)
                .map(p -> DtoMapperPosition.builder().setPosition(p).build());  // Mejor nomenclatura
    }

    @Override
    public PositionResponse addPosition(PositionEntity position) {
        if (positionRepository.existsByName(position.getName())) {
            throw new RuntimeException("El cargo ya existe");
        }
        return DtoMapperPosition.builder()
                .setPosition(positionRepository.save(position))  // Mejor nomenclatura
                .build();
    }

    @Override
    public Optional<PositionResponse> updatePosition(Long id, PositionRequest positionRequest) {
        Optional<PositionEntity> positionOptional = positionRepository.findById(id);
        if (!positionOptional.isPresent()) {
            throw new RuntimeException("El cargo no fue encontrado");
        }

        PositionEntity positionEntity = positionOptional.orElseThrow();
        positionEntity.setName(positionRequest.getName());
        PositionEntity updatedPosition = positionRepository.save(positionEntity);

        return Optional.of(DtoMapperPosition.builder().setPosition(updatedPosition).build());  // Mejor nomenclatura
    }

    @Override
    public void deletePosition(Long id) {
        if (!positionRepository.existsById(id)) {
            throw new RuntimeException("El cargo no fue encontrado para eliminar");
        }
        positionRepository.deleteById(id);
    }

    @Override
    public Optional<PositionResponse    > findPositionByName(String name) {
        return positionRepository.findByName(name)
                .map(positionEntity -> DtoMapperPosition.builder()
                        .setPosition(positionEntity)
                        .build());
    }
}
