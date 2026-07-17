package com.tractus.backend.controllers;

import com.tractus.backend.models.Thread;
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
    public List<Thread> getThreadsBySpace(@PathVariable Long spaceId) {
        return threadService.getThreadsBySpace(spaceId);
    }

    @PostMapping
    public Thread createThread(@RequestBody Thread thread) {
        return threadService.createThread(thread);
    }
}
