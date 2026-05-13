package edu.thu.canteen.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public final class ReviewDtos {
    private ReviewDtos() {
    }

    public record CreateReviewRequest(
            @NotNull @Min(1) @Max(5) Integer rating,
            @NotBlank @Size(min = 2, max = 500) String content,
            @Size(max = 255) String imageUrl
    ) {
    }

    public record VoteRequest(@NotNull @Min(-1) @Max(1) Integer vote) {
    }

    public record ReviewView(
            Long id,
            Long dishId,
            Long userId,
            String nickname,
            Integer rating,
            String content,
            String imageUrl,
            Long upVotes,
            Long downVotes,
            LocalDateTime createdAt
    ) {
    }
}
