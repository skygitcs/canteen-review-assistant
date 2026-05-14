package edu.thu.canteen.data.model;

public class UserProfile {
    public final String username;
    public final String nickname;
    public final String tastePreference;
    public final String avatarUrl;

    public UserProfile(String username, String nickname, String tastePreference, String avatarUrl) {
        this.username = username;
        this.nickname = nickname;
        this.tastePreference = tastePreference;
        this.avatarUrl = avatarUrl;
    }
}

