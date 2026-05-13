package edu.thu.canteen.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import edu.thu.canteen.common.BusinessException;
import edu.thu.canteen.domain.entity.Dish;
import edu.thu.canteen.domain.entity.Favorite;
import edu.thu.canteen.domain.entity.User;
import edu.thu.canteen.domain.mapper.DishMapper;
import edu.thu.canteen.domain.mapper.FavoriteMapper;
import edu.thu.canteen.domain.mapper.UserMapper;
import edu.thu.canteen.dto.DishDtos;
import edu.thu.canteen.dto.UserDtos;
import edu.thu.canteen.dto.UserProfile;
import edu.thu.canteen.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final DishMapper dishMapper;
    private final FavoriteMapper favoriteMapper;
    private final AuthService authService;
    private final DishViewService dishViewService;

    public UserService(UserMapper userMapper, DishMapper dishMapper, FavoriteMapper favoriteMapper,
                       AuthService authService, DishViewService dishViewService) {
        this.userMapper = userMapper;
        this.dishMapper = dishMapper;
        this.favoriteMapper = favoriteMapper;
        this.authService = authService;
        this.dishViewService = dishViewService;
    }

    public UserProfile me() {
        return authService.profileOf(SecurityUtils.currentUser());
    }

    public UserProfile update(UserDtos.UpdateProfileRequest request) {
        User user = SecurityUtils.currentUser();
        if (request.nickname() != null) {
            user.setNickname(request.nickname());
        }
        if (request.avatarUrl() != null) {
            user.setAvatarUrl(request.avatarUrl());
        }
        if (request.tastePreference() != null) {
            user.setTastePreference(request.tastePreference());
        }
        if (request.campusCardAuthorized() != null) {
            user.setCampusCardAuthorized(request.campusCardAuthorized());
        }
        userMapper.updateById(user);
        return authService.profileOf(user);
    }

    public void favorite(Long dishId) {
        Dish dish = dishMapper.selectById(dishId);
        if (dish == null || !"APPROVED".equals(dish.getStatus())) {
            throw BusinessException.notFound("dish not found");
        }
        Long userId = SecurityUtils.currentUserId();
        Long exists = favoriteMapper.selectCount(Wrappers.<Favorite>lambdaQuery()
                .eq(Favorite::getDishId, dishId)
                .eq(Favorite::getUserId, userId));
        if (exists == 0) {
            Favorite favorite = new Favorite();
            favorite.setDishId(dishId);
            favorite.setUserId(userId);
            favoriteMapper.insert(favorite);
        }
    }

    public void unfavorite(Long dishId) {
        favoriteMapper.delete(Wrappers.<Favorite>lambdaQuery()
                .eq(Favorite::getDishId, dishId)
                .eq(Favorite::getUserId, SecurityUtils.currentUserId()));
    }

    public List<DishDtos.DishCard> favorites() {
        return favoriteMapper.selectList(Wrappers.<Favorite>lambdaQuery()
                        .eq(Favorite::getUserId, SecurityUtils.currentUserId())
                        .orderByDesc(Favorite::getCreatedAt))
                .stream()
                .map(f -> dishMapper.selectById(f.getDishId()))
                .filter(d -> d != null && "APPROVED".equals(d.getStatus()))
                .map(dishViewService::card)
                .toList();
    }
}
