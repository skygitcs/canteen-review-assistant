package edu.thu.canteen.data.network;

import edu.thu.canteen.data.model.UserProfile;

public class AuthDtos {
    public static class LoginRequest {
        public String username;
        public String password;

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    public static class RegisterRequest {
        public String username;
        public String password;
        public String nickname;

        public RegisterRequest(String username, String password, String nickname) {
            this.username = username;
            this.password = password;
            this.nickname = nickname;
        }
    }

    public static class AuthResponse {
        public String token;
        public UserProfile user;
    }
}
