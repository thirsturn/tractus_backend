package com.tractus.backend.controllers;

import com.tractus.backend.models.Space;
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
    public List<Space> getAllSpaces() {
        return spaceService.getAllSpaces();
    }

    @PostMapping
    public Space createSpace(@RequestBody Space space) {
        return spaceService.createSpace(space);
    }
}
