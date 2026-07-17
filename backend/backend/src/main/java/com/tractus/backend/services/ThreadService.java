package com.tractus.backend.services;

import com.tractus.backend.models.Thread;
import com.tractus.backend.repositories.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThreadService {
    
    @Autowired
    private ThreadRepository threadRepository;

    public List<Thread> getThreadsBySpace(Long spaceId) {
        return threadRepository.findBySpaceId(spaceId);
    }
    
    public Thread createThread(Thread thread) {
        return threadRepository.save(thread);
    }
}
