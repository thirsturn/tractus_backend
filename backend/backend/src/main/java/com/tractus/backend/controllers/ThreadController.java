package com.tractus.backend.controllers;

import com.tractus.backend.dtos.ThreadCreateRequest;
import com.tractus.backend.dtos.ThreadResponse;
import com.tractus.backend.services.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/threads")
public class ThreadController {

    @Autowired
    private ThreadService threadService;

    @GetMapping("/space/{spaceId}")
    public List<ThreadResponse> getThreadsBySpace(@PathVariable Long spaceId) {
        return threadService.getThreadsBySpace(spaceId);
    }

    @PostMapping
    public ThreadResponse createThread(@RequestBody ThreadCreateRequest request) {
        return threadService.createThread(request);
    }

    @GetMapping("/{id}")
    public ThreadResponse getThreadById(@PathVariable Long id) {
        return threadService.getThreadById(id);
    }
}
