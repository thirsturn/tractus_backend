package com.tractus.backend.dtos;

import com.tractus.backend.models.VoteType;
import lombok.Data;

@Data
public class VoteResponse {
    private Long id;
    private Long userId;
    private Long targetId;
    private VoteType voteType;
}
