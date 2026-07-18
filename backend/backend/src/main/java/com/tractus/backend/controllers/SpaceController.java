package com.tractus.backend.controllers;

import com.tractus.backend.dtos.SpaceCreateRequest;
import com.tractus.backend.dtos.SpaceResponse;
import com.tractus.backend.services.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spaces")
public class SpaceController {

    @Autowired
    private SpaceService spaceService;

    @GetMapping
    public List<SpaceResponse> getAllSpaces() {
        return spaceService.getAllSpaces();
    }

    @PostMapping
    public SpaceResponse createSpace(@RequestBody SpaceCreateRequest request) {
        return spaceService.createSpace(request);
    }
}
