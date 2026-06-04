package edu.thu.canteen.controller;

import edu.thu.canteen.common.ApiResponse;
import edu.thu.canteen.domain.entity.DishSubmission;
import edu.thu.canteen.dto.DishDtos;
import edu.thu.canteen.service.DishService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
public class DishController {
    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping
    public ApiResponse<List<DishDtos.DishCard>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long canteenId,
            @RequestParam(required = false) Integer floorNo,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String sort) {
        return ApiResponse.ok(dishService.list(keyword, canteenId, floorNo, tag, sort));
    }

    @GetMapping("/{id}")
    public ApiResponse<DishDtos.DishDetail> detail(@PathVariable Long id) {
        return ApiResponse.ok(dishService.detail(id));
    }

    @GetMapping("/recommendations")
    public ApiResponse<List<DishDtos.DishCard>> recommendations(@RequestParam(required = false) Integer limit) {
        return ApiResponse.ok(dishService.recommendations(limit));
    }

    @GetMapping("/tags")
    public ApiResponse<List<String>> tags() {
        return ApiResponse.ok(dishService.tags());
    }

    @PostMapping("/submissions")
    public ApiResponse<DishSubmission> submit(@Valid @RequestBody DishDtos.DishSubmissionRequest request) {
        return ApiResponse.ok(dishService.submit(request));
    }
}
