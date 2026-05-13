package edu.thu.canteen.controller;

import edu.thu.canteen.common.ApiResponse;
import edu.thu.canteen.dto.DishDtos;
import edu.thu.canteen.dto.UserDtos;
import edu.thu.canteen.dto.UserProfile;
import edu.thu.canteen.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ApiResponse<UserProfile> me() {
        return ApiResponse.ok(userService.me());
    }

    @PutMapping("/me")
    public ApiResponse<UserProfile> update(@Valid @RequestBody UserDtos.UpdateProfileRequest request) {
        return ApiResponse.ok(userService.update(request));
    }

    @GetMapping("/me/favorites")
    public ApiResponse<List<DishDtos.DishCard>> favorites() {
        return ApiResponse.ok(userService.favorites());
    }

    @PostMapping("/me/favorites")
    public ApiResponse<Void> favorite(@Valid @RequestBody UserDtos.FavoriteRequest request) {
        userService.favorite(request.dishId());
        return ApiResponse.ok();
    }

    @DeleteMapping("/me/favorites/{dishId}")
    public ApiResponse<Void> unfavorite(@PathVariable Long dishId) {
        userService.unfavorite(dishId);
        return ApiResponse.ok();
    }
}
