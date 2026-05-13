package edu.thu.canteen.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import edu.thu.canteen.common.BusinessException;
import edu.thu.canteen.domain.entity.User;
import edu.thu.canteen.domain.mapper.UserMapper;
import edu.thu.canteen.dto.AuthDtos;
import edu.thu.canteen.dto.UserProfile;
import edu.thu.canteen.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request) {
        Long exists = userMapper.selectCount(Wrappers.<User>lambdaQuery().eq(User::getUsername, request.username()));
        if (exists > 0) {
            throw BusinessException.conflict("username already exists");
        }
        User user = new User();
        user.setUsername(request.username());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setNickname(request.nickname());
        user.setRole("USER");
        user.setStatus(1);
        user.setCampusCardAuthorized(false);
        userMapper.insert(user);
        return tokenOf(user);
    }

    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, request.username()));
        if (user == null || user.getStatus() != 1 || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw BusinessException.unauthorized("invalid username or password");
        }
        return tokenOf(user);
    }

    public UserProfile profileOf(User user) {
        return new UserProfile(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getAvatarUrl(),
                user.getRole(),
                user.getTastePreference(),
                user.getCampusCardAuthorized()
        );
    }

    private AuthDtos.AuthResponse tokenOf(User user) {
        String token = jwtService.generate(user.getId(), user.getUsername(), user.getRole());
        return new AuthDtos.AuthResponse(token, profileOf(user));
    }
}
