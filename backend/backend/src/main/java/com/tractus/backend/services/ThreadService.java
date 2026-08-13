package com.tractus.backend.services;

import com.tractus.backend.dtos.ThreadCreateRequest;
import com.tractus.backend.dtos.ThreadResponse;
import com.tractus.backend.mappers.ThreadMapper;
import com.tractus.backend.models.Space;
import com.tractus.backend.models.Thread;
import com.tractus.backend.models.User;
import com.tractus.backend.repositories.SpaceRepository;
import com.tractus.backend.repositories.ThreadRepository;
import com.tractus.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThreadService {
    
    @Autowired
    private ThreadRepository threadRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SpaceRepository spaceRepository;
    @Autowired
    private ThreadMapper threadMapper;

    public List<ThreadResponse> getThreadsBySpace(Long spaceId) {
        return threadRepository.findBySpaceId(spaceId).stream()
                .map(threadMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ThreadResponse getThreadById(Long id) {
        Thread thread = threadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Thread not found"));
        return threadMapper.toResponse(thread);
    }
    
    @Autowired
    private FileStorageService fileStorageService;

    public ThreadResponse createThread(ThreadCreateRequest request, org.springframework.web.multipart.MultipartFile image) {
        Thread thread = threadMapper.toEntity(request);
        
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Space space = spaceRepository.findById(request.getSpaceId())
                .orElseThrow(() -> new RuntimeException("Space not found"));
                
        thread.setUser(user);
        thread.setSpace(space);

        if (image != null && !image.isEmpty()) {
            String imageUrl = fileStorageService.storeFile(image);
            thread.setImageUrl("http://localhost:8081" + imageUrl);
        } else if (request.getImageUrl() != null) {
            thread.setImageUrl(request.getImageUrl());
        }
        
        Thread savedThread = threadRepository.save(thread);
        return threadMapper.toResponse(savedThread);
    }
}
