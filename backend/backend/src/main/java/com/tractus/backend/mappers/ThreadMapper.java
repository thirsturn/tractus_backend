package com.tractus.backend.mappers;

import com.tractus.backend.dtos.ThreadCreateRequest;
import com.tractus.backend.dtos.ThreadResponse;
import com.tractus.backend.models.Thread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThreadMapper {

    @Autowired
    private UserMapper userMapper;

    public Thread toEntity(ThreadCreateRequest request) {
        if (request == null) {
            return null;
        }
        Thread thread = new Thread();
        thread.setTitle(request.getTitle());
        thread.setContent(request.getContent());
        thread.setImageUrl(request.getImageUrl());
        // Note: userId and spaceId will be mapped in the Service layer by fetching from DB
        return thread;
    }

    public ThreadResponse toResponse(Thread thread) {
        if (thread == null) {
            return null;
        }
        ThreadResponse response = new ThreadResponse();
        response.setId(thread.getId());
        response.setTitle(thread.getTitle());
        response.setContent(thread.getContent());
        response.setImageUrl(thread.getImageUrl());
        if (thread.getUser() != null) {
            response.setAuthor(userMapper.toResponse(thread.getUser()));
        }
        if (thread.getSpace() != null) {
            response.setSpaceId(thread.getSpace().getId());
        }
        if (thread.getCreatedAt() != null) {
            response.setCreatedAt(thread.getCreatedAt().toString());
        }
        if (thread.getComments() != null) {
            response.setCommentCount(thread.getComments().size());
        }
        return response;
    }
}
