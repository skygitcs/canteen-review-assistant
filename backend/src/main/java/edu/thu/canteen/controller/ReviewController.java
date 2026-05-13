package edu.thu.canteen.controller;

import edu.thu.canteen.common.ApiResponse;
import edu.thu.canteen.dto.ReviewDtos;
import edu.thu.canteen.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/api/dishes/{dishId}/reviews")
    public ApiResponse<ReviewDtos.ReviewView> create(@PathVariable Long dishId,
                                                     @Valid @RequestBody ReviewDtos.CreateReviewRequest request) {
        return ApiResponse.ok(reviewService.create(dishId, request));
    }

    @PostMapping("/api/reviews/{reviewId}/vote")
    public ApiResponse<ReviewDtos.ReviewView> vote(@PathVariable Long reviewId,
                                                   @Valid @RequestBody ReviewDtos.VoteRequest request) {
        return ApiResponse.ok(reviewService.vote(reviewId, request.vote()));
    }
}
