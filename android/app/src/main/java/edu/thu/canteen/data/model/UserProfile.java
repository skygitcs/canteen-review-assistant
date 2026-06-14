package edu.thu.canteen.data.model;

public class UserProfile {
    public final String username;
    public final String nickname;
    public final String tastePreference;
    public final String avatarUrl;
    public final String role; // USER or ADMIN

    public UserProfile(String username, String nickname, String tastePreference, String avatarUrl, String role) {
        this.username = username;
        this.nickname = nickname;
        this.tastePreference = tastePreference;
        this.avatarUrl = avatarUrl;
        this.role = role;
    }
}

