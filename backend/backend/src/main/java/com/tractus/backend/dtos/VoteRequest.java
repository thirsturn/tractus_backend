package com.tractus.backend.dtos;

import com.tractus.backend.models.VoteType;
import lombok.Data;

@Data
public class VoteRequest {
    private Long userId;
    private Long targetId;
    private VoteType voteType;
}
