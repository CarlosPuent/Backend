package com.usuarios.apiUsuarios.mapper;
import com.usuarios.apiUsuarios.model.dto.PositionResponse;
import com.usuarios.apiUsuarios.model.entity.PositionEntity;
import lombok.Setter;

@Setter
public class DtoMapperPosition {

    private PositionEntity position;

    public DtoMapperPosition() {}

    public static DtoMapperPosition builder() {
        return new DtoMapperPosition();
    }

    public DtoMapperPosition setPosition(PositionEntity position) {  // Cambio de nombre
        this.position = position;
        return this;
    }

    public PositionResponse build() {
        if (position == null) {
            throw new RuntimeException("must pass the entity position!");
        }
        return new PositionResponse(this.position.getId(), position.getName());
    }
}
