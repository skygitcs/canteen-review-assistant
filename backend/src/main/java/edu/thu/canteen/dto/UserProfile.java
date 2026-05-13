package edu.thu.canteen.dto;

public record UserProfile(
        Long id,
        String username,
        String nickname,
        String avatarUrl,
        String role,
        String tastePreference,
        Boolean campusCardAuthorized
) {
}
