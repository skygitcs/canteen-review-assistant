package edu.thu.canteen.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import edu.thu.canteen.domain.entity.*;
import edu.thu.canteen.domain.mapper.*;
import edu.thu.canteen.dto.DishDtos;
import edu.thu.canteen.dto.ReviewDtos;
import edu.thu.canteen.security.SecurityUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class DishViewService {
    private final DishMapper dishMapper;
    private final CanteenMapper canteenMapper;
    private final CanteenWindowMapper windowMapper;
    private final DishTagMapper dishTagMapper;
    private final ReviewMapper reviewMapper;
    private final ReviewVoteMapper voteMapper;
    private final FavoriteMapper favoriteMapper;
    private final UserMapper userMapper;

    public DishViewService(DishMapper dishMapper, CanteenMapper canteenMapper, CanteenWindowMapper windowMapper,
                           DishTagMapper dishTagMapper, ReviewMapper reviewMapper, ReviewVoteMapper voteMapper,
                           FavoriteMapper favoriteMapper, UserMapper userMapper) {
        this.dishMapper = dishMapper;
        this.canteenMapper = canteenMapper;
        this.windowMapper = windowMapper;
        this.dishTagMapper = dishTagMapper;
        this.reviewMapper = reviewMapper;
        this.voteMapper = voteMapper;
        this.favoriteMapper = favoriteMapper;
        this.userMapper = userMapper;
    }

    public DishDtos.DishCard card(Dish dish) {
        Canteen canteen = canteenMapper.selectById(dish.getCanteenId());
        CanteenWindow window = dish.getWindowId() == null ? null : windowMapper.selectById(dish.getWindowId());
        List<String> tags = dishTagMapper.selectList(Wrappers.<DishTag>lambdaQuery().eq(DishTag::getDishId, dish.getId()))
                .stream().map(DishTag::getTag).toList();
        List<Review> reviews = approvedReviews(dish.getId());
        double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
        Long favorites = favoriteMapper.selectCount(Wrappers.<Favorite>lambdaQuery().eq(Favorite::getDishId, dish.getId()));
        return new DishDtos.DishCard(
                dish.getId(),
                dish.getCanteenId(),
                canteen == null ? null : canteen.getName(),
                dish.getWindowId(),
                window == null ? null : window.getName(),
                window == null ? null : window.getFloorNo(),
                dish.getName(),
                dish.getImageUrl(),
                dish.getPrice(),
                dish.getDescription(),
                dish.getSpiceLevel(),
                tags,
                round(avg),
                (long) reviews.size(),
                favorites
        );
    }

    public DishDtos.DishSubmissionView submissionView(DishSubmission submission) {
        User user = userMapper.selectById(submission.getSubmitterId());
        Canteen canteen = canteenMapper.selectById(submission.getCanteenId());
        CanteenWindow window = submission.getWindowId() == null ? null : windowMapper.selectById(submission.getWindowId());
        List<String> tags = submission.getTags() == null ? List.of() :
                java.util.Arrays.stream(submission.getTags().split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList();
        return new DishDtos.DishSubmissionView(
                submission.getId(),
                submission.getSubmitterId(),
                user == null ? "unknown" : user.getNickname(),
                submission.getCanteenId(),
                canteen == null ? null : canteen.getName(),
                submission.getWindowId(),
                window == null ? null : window.getName(),
                window == null ? null : window.getFloorNo(),
                submission.getName(),
                submission.getImageUrl(),
                submission.getPrice(),
                submission.getDescription(),
                submission.getSpiceLevel(),
                tags,
                submission.getAuditStatus(),
                submission.getAuditReason(),
                submission.getCreatedAt()
        );
    }

    public List<ReviewDtos.ReviewView> reviewViews(Long dishId) {
        return approvedReviews(dishId).stream()
                .sorted(Comparator.comparing(Review::getCreatedAt).reversed())
                .map(this::reviewView)
                .toList();
    }

    public ReviewDtos.ReviewView reviewView(Review review) {
        User user = userMapper.selectById(review.getUserId());
        Long up = voteMapper.selectCount(Wrappers.<ReviewVote>lambdaQuery()
                .eq(ReviewVote::getReviewId, review.getId()).eq(ReviewVote::getVote, 1));
        Long down = voteMapper.selectCount(Wrappers.<ReviewVote>lambdaQuery()
                .eq(ReviewVote::getReviewId, review.getId()).eq(ReviewVote::getVote, -1));
        Integer myVote = currentUserVote(review.getId());
        return new ReviewDtos.ReviewView(
                review.getId(),
                review.getDishId(),
                review.getUserId(),
                user == null ? "unknown" : user.getNickname(),
                review.getRating(),
                review.getContent(),
                review.getImageUrl(),
                up,
                down,
                myVote,
                review.getStatus(),
                review.getCreatedAt()
        );
    }

    public ReviewDtos.AdminReviewView adminReviewView(Review review) {
        User user = userMapper.selectById(review.getUserId());
        Dish dish = dishMapper.selectById(review.getDishId());
        Long up = voteMapper.selectCount(Wrappers.<ReviewVote>lambdaQuery()
                .eq(ReviewVote::getReviewId, review.getId()).eq(ReviewVote::getVote, 1));
        Long down = voteMapper.selectCount(Wrappers.<ReviewVote>lambdaQuery()
                .eq(ReviewVote::getReviewId, review.getId()).eq(ReviewVote::getVote, -1));
        return new ReviewDtos.AdminReviewView(
                review.getId(),
                review.getDishId(),
                dish == null ? "unknown" : dish.getName(),
                review.getUserId(),
                user == null ? "unknown" : user.getNickname(),
                review.getRating(),
                review.getContent(),
                review.getImageUrl(),
                up,
                down,
                review.getStatus(),
                review.getCreatedAt()
        );
    }

    public double weightedScore(DishDtos.DishCard card) {
        return card.avgRating() * 10 + card.favoriteCount() * 1.5 + card.reviewCount();
    }

    private List<Review> approvedReviews(Long dishId) {
        return reviewMapper.selectList(Wrappers.<Review>lambdaQuery()
                .eq(Review::getDishId, dishId)
                .eq(Review::getStatus, "APPROVED"));
    }

    private Integer currentUserVote(Long reviewId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof User)) {
            return 0;
        }
        ReviewVote vote = voteMapper.selectOne(Wrappers.<ReviewVote>lambdaQuery()
                .eq(ReviewVote::getReviewId, reviewId)
                .eq(ReviewVote::getUserId, SecurityUtils.currentUserId()));
        return vote == null ? 0 : vote.getVote();
    }

    private Double round(double value) {
        return BigDecimal.valueOf(value).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }
}
