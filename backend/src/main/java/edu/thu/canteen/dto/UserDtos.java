package edu.thu.canteen.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

public final class UserDtos {
    private UserDtos() {
    }

    public record UpdateProfileRequest(
            @Size(max = 32) String nickname,
            @Size(max = 255) String avatarUrl,
            @Size(max = 128) String tastePreference,
            Boolean campusCardAuthorized
    ) {
    }

    public record FavoriteRequest(@NotNull Long dishId) {
    }
}
