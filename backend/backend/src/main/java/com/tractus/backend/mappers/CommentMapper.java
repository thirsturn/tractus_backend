package com.tractus.backend.mappers;

import com.tractus.backend.dtos.CommentCreateRequest;
import com.tractus.backend.dtos.CommentResponse;
import com.tractus.backend.models.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    @Autowired
    private UserMapper userMapper;

    public Comment toEntity(CommentCreateRequest request) {
        if (request == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        // Note: userId, threadId, and parentCommentId will be mapped in the Service layer
        return comment;
    }

    public CommentResponse toResponse(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setAuthor(userMapper.toResponse(comment.getUser()));
        if (comment.getThread() != null) {
            response.setThreadId(comment.getThread().getId());
        }
        if (comment.getParentComment() != null) {
            response.setParentCommentId(comment.getParentComment().getId());
        }
        return response;
    }
}
