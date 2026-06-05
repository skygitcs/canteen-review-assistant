package edu.thu.canteen.controller;

import edu.thu.canteen.common.ApiResponse;
import edu.thu.canteen.domain.entity.Announcement;
import edu.thu.canteen.domain.entity.Dish;
import edu.thu.canteen.domain.entity.DishSubmission;
import edu.thu.canteen.dto.AdminDtos;
import edu.thu.canteen.dto.DishDtos;
import edu.thu.canteen.service.AnnouncementService;
import edu.thu.canteen.service.DishService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final DishService dishService;
    private final AnnouncementService announcementService;

    public AdminController(DishService dishService, AnnouncementService announcementService) {
        this.dishService = dishService;
        this.announcementService = announcementService;
    }

    @GetMapping("/submissions")
    public ApiResponse<List<DishDtos.DishSubmissionView>> pendingSubmissions() {
        return ApiResponse.ok(dishService.pendingSubmissions());
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
}
