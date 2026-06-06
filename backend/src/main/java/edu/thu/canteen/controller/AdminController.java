package edu.thu.canteen.controller;

import edu.thu.canteen.common.ApiResponse;
import edu.thu.canteen.domain.entity.Announcement;
import edu.thu.canteen.domain.entity.Dish;
import edu.thu.canteen.domain.entity.DishSubmission;
import edu.thu.canteen.dto.AdminDtos;
import edu.thu.canteen.dto.DishDtos;
import edu.thu.canteen.dto.ReviewDtos;
import edu.thu.canteen.service.AnnouncementService;
import edu.thu.canteen.service.DishService;
import edu.thu.canteen.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final DishService dishService;
    private final ReviewService reviewService;
    private final AnnouncementService announcementService;

    public AdminController(DishService dishService, ReviewService reviewService, AnnouncementService announcementService) {
        this.dishService = dishService;
        this.reviewService = reviewService;
        this.announcementService = announcementService;
    }

    @GetMapping("/submissions")
    public ApiResponse<List<DishDtos.DishSubmissionView>> submissions(@RequestParam(required = false) String status) {
        return ApiResponse.ok(dishService.submissions(status));
    }

    @PostMapping("/submissions/{id}/approve")
    public ApiResponse<Dish> approve(@PathVariable Long id, @Valid @RequestBody AdminDtos.AuditRequest request) {
        return ApiResponse.ok(dishService.approveSubmission(id, request.reason()));
    }

    @PostMapping("/submissions/{id}/reject")
    public ApiResponse<Void> reject(@PathVariable Long id, @Valid @RequestBody AdminDtos.AuditRequest request) {
        dishService.rejectSubmission(id, request.reason());
        return ApiResponse.ok();
    }

    @PostMapping("/announcements")
    public ApiResponse<Announcement> createAnnouncement(@Valid @RequestBody AdminDtos.AnnouncementRequest request) {
        return ApiResponse.ok(announcementService.create(request));
    }

    @GetMapping("/reviews")
    public ApiResponse<List<ReviewDtos.AdminReviewView>> reviews(@RequestParam(required = false) String status) {
        return ApiResponse.ok(reviewService.listForAdmin(status));
    }

    @PostMapping("/reviews/{id}/approve")
    public ApiResponse<ReviewDtos.AdminReviewView> approveReview(@PathVariable Long id) {
        return ApiResponse.ok(reviewService.approve(id));
    }

    @PostMapping("/reviews/{id}/reject")
    public ApiResponse<ReviewDtos.AdminReviewView> rejectReview(@PathVariable Long id,
                                                                @Valid @RequestBody AdminDtos.AuditRequest request) {
        return ApiResponse.ok(reviewService.reject(id, request.reason()));
    }
}
