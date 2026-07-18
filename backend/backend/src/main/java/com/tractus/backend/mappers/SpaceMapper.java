package com.tractus.backend.mappers;

import com.tractus.backend.dtos.SpaceCreateRequest;
import com.tractus.backend.dtos.SpaceResponse;
import com.tractus.backend.models.Space;
import org.springframework.stereotype.Component;

@Component
public class SpaceMapper {

    public Space toEntity(SpaceCreateRequest request) {
        if (request == null) {
            return null;
        }
        Space space = new Space();
        space.setName(request.getName());
        space.setDescription(request.getDescription());
        return space;
    }

    public SpaceResponse toResponse(Space space) {
        if (space == null) {
            return null;
        }
        SpaceResponse response = new SpaceResponse();
        response.setId(space.getId());
        response.setName(space.getName());
        response.setDescription(space.getDescription());
        return response;
    }
}
