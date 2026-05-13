package edu.thu.canteen.controller;

import edu.thu.canteen.common.ApiResponse;
import edu.thu.canteen.domain.entity.Announcement;
import edu.thu.canteen.service.AnnouncementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {
    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    public ApiResponse<List<Announcement>> active() {
        return ApiResponse.ok(announcementService.active());
    }
}
