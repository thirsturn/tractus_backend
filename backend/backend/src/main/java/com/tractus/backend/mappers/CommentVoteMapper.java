package com.tractus.backend.mappers;

import com.tractus.backend.dtos.VoteRequest;
import com.tractus.backend.dtos.VoteResponse;
import com.tractus.backend.models.CommentVote;
import org.springframework.stereotype.Component;

@Component
public class CommentVoteMapper {

    public CommentVote toEntity(VoteRequest request) {
        if (request == null) {
            return null;
        }
        CommentVote vote = new CommentVote();
        vote.setVoteType(request.getVoteType());
        // userId and commentId mapped in service layer
        return vote;
    }

    public VoteResponse toResponse(CommentVote vote) {
        if (vote == null) {
            return null;
        }
        VoteResponse response = new VoteResponse();
        response.setId(vote.getId());
        response.setVoteType(vote.getVoteType());
        if (vote.getUser() != null) {
            response.setUserId(vote.getUser().getId());
        }
        if (vote.getComment() != null) {
            response.setTargetId(vote.getComment().getId());
        }
        return response;
    }
}
