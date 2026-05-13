package edu.thu.canteen.controller;

import edu.thu.canteen.common.ApiResponse;
import edu.thu.canteen.dto.CanteenDtos;
import edu.thu.canteen.service.CanteenService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/canteens")
public class CanteenController {
    private final CanteenService canteenService;

    public CanteenController(CanteenService canteenService) {
        this.canteenService = canteenService;
    }

    @GetMapping
    public ApiResponse<List<CanteenDtos.CanteenCard>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean onCampus,
            @RequestParam(required = false) String sort) {
        return ApiResponse.ok(canteenService.list(keyword, onCampus, sort));
    }

    @GetMapping("/{id}")
    public ApiResponse<CanteenDtos.CanteenDetail> detail(
            @PathVariable Long id,
            @RequestParam(required = false) Integer floorNo,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String sort) {
        return ApiResponse.ok(canteenService.detail(id, floorNo, tag, sort));
    }

    @PostMapping("/{id}/crowd")
    public ApiResponse<Void> reportCrowd(@PathVariable Long id, @Valid @RequestBody CanteenDtos.CrowdReportRequest request) {
        canteenService.reportCrowd(id, request.level());
        return ApiResponse.ok();
    }

    @GetMapping("/heatmap")
    public ApiResponse<List<CanteenDtos.HeatPoint>> heatmap(@RequestParam(defaultValue = "global") String scope) {
        return ApiResponse.ok(canteenService.heatmap(scope));
    }
}
