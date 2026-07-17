package com.tractus.backend.services;

import com.tractus.backend.models.Space;
import com.tractus.backend.repositories.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpaceService {
    
    @Autowired
    private SpaceRepository spaceRepository;

    public List<Space> getAllSpaces() {
        return spaceRepository.findAll(); // Fetches all spaces from DB
    }
    
    public Space createSpace(Space space) {
        return spaceRepository.save(space);
    }
}