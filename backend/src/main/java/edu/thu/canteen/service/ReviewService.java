package edu.thu.canteen.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import edu.thu.canteen.common.BusinessException;
import edu.thu.canteen.domain.entity.Dish;
import edu.thu.canteen.domain.entity.Review;
import edu.thu.canteen.domain.entity.ReviewVote;
import edu.thu.canteen.domain.mapper.DishMapper;
import edu.thu.canteen.domain.mapper.ReviewMapper;
import edu.thu.canteen.domain.mapper.ReviewVoteMapper;
import edu.thu.canteen.dto.ReviewDtos;
import edu.thu.canteen.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {
    private final DishMapper dishMapper;
    private final ReviewMapper reviewMapper;
    private final ReviewVoteMapper voteMapper;
    private final DishViewService dishViewService;

    public ReviewService(DishMapper dishMapper, ReviewMapper reviewMapper, ReviewVoteMapper voteMapper,
                         DishViewService dishViewService) {
        this.dishMapper = dishMapper;
        this.reviewMapper = reviewMapper;
        this.voteMapper = voteMapper;
        this.dishViewService = dishViewService;
    }

    public ReviewDtos.ReviewView create(Long dishId, ReviewDtos.CreateReviewRequest request) {
        Dish dish = dishMapper.selectById(dishId);
        if (dish == null || !"APPROVED".equals(dish.getStatus())) {
            throw BusinessException.notFound("dish not found");
        }
        Long userId = SecurityUtils.currentUserId();
        Long duplicate = reviewMapper.selectCount(Wrappers.<Review>lambdaQuery()
                .eq(Review::getDishId, dishId)
                .eq(Review::getUserId, userId)
                .ne(Review::getStatus, "DELETED"));
        if (duplicate > 0) {
            throw BusinessException.conflict("one user can review a dish only once");
        }
        Review review = new Review();
        review.setDishId(dishId);
        review.setUserId(userId);
        review.setRating(request.rating());
        review.setContent(request.content().trim());
        review.setImageUrl(request.imageUrl());
        review.setStatus("APPROVED");
        reviewMapper.insert(review);
        return dishViewService.reviewView(review);
    }

    @Transactional
    public ReviewDtos.ReviewView vote(Long reviewId, Integer vote) {
        if (vote == 0) {
            throw BusinessException.badRequest("vote must be 1 or -1");
        }
        Review review = reviewMapper.selectById(reviewId);
        if (review == null || !"APPROVED".equals(review.getStatus())) {
            throw BusinessException.notFound("review not found");
        }
        Long userId = SecurityUtils.currentUserId();
        ReviewVote existing = voteMapper.selectOne(Wrappers.<ReviewVote>lambdaQuery()
                .eq(ReviewVote::getReviewId, reviewId)
                .eq(ReviewVote::getUserId, userId));
        if (existing == null) {
            ReviewVote entity = new ReviewVote();
            entity.setReviewId(reviewId);
            entity.setUserId(userId);
            entity.setVote(vote);
            voteMapper.insert(entity);
        } else {
            existing.setVote(vote);
            voteMapper.updateById(existing);
        }
        return dishViewService.reviewView(review);
    }
}
