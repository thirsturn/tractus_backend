package com.tractus.backend.services;

import com.tractus.backend.dtos.SpaceCreateRequest;
import com.tractus.backend.dtos.SpaceResponse;
import com.tractus.backend.mappers.SpaceMapper;
import com.tractus.backend.models.Space;
import com.tractus.backend.repositories.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpaceService {
    
    @Autowired
    private SpaceRepository spaceRepository;
    
    @Autowired
    private SpaceMapper spaceMapper;

    public List<SpaceResponse> getAllSpaces() {
        return spaceRepository.findAll().stream()
                .map(spaceMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    public SpaceResponse createSpace(SpaceCreateRequest request) {
        Space space = spaceMapper.toEntity(request);
        Space savedSpace = spaceRepository.save(space);
        return spaceMapper.toResponse(savedSpace);
    }
}