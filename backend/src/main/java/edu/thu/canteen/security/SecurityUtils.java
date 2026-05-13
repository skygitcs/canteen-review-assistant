package edu.thu.canteen.security;

import edu.thu.canteen.common.BusinessException;
import edu.thu.canteen.domain.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {
    private SecurityUtils() {
    }

    public static User currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw BusinessException.unauthorized("login required");
        }
        return user;
    }

    public static Long currentUserId() {
        return currentUser().getId();
    }
}
