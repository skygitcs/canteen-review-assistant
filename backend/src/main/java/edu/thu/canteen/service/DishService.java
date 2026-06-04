package edu.thu.canteen.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import edu.thu.canteen.common.BusinessException;
import edu.thu.canteen.domain.entity.*;
import edu.thu.canteen.domain.mapper.*;
import edu.thu.canteen.dto.DishDtos;
import edu.thu.canteen.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class DishService {
    private final DishMapper dishMapper;
    private final CanteenMapper canteenMapper;
    private final CanteenWindowMapper windowMapper;
    private final DishTagMapper dishTagMapper;
    private final DishSubmissionMapper submissionMapper;
    private final DishViewService dishViewService;

    public DishService(DishMapper dishMapper, CanteenMapper canteenMapper, CanteenWindowMapper windowMapper,
                       DishTagMapper dishTagMapper, DishSubmissionMapper submissionMapper, DishViewService dishViewService) {
        this.dishMapper = dishMapper;
        this.canteenMapper = canteenMapper;
        this.windowMapper = windowMapper;
        this.dishTagMapper = dishTagMapper;
        this.submissionMapper = submissionMapper;
        this.dishViewService = dishViewService;
    }

    public List<DishDtos.DishCard> list(String keyword, Long canteenId, Integer floorNo, String tag, String sort) {
        LambdaQueryWrapper<Dish> query = Wrappers.<Dish>lambdaQuery().eq(Dish::getStatus, "APPROVED");
        if (keyword != null && !keyword.isBlank()) {
            query.like(Dish::getName, keyword.trim());
        }
        if (canteenId != null) {
            query.eq(Dish::getCanteenId, canteenId);
        }
        Stream<DishDtos.DishCard> stream = dishMapper.selectList(query).stream().map(dishViewService::card);
        if (floorNo != null) {
            stream = stream.filter(d -> floorNo.equals(d.floorNo()));
        }
        if (tag != null && !tag.isBlank()) {
            stream = stream.filter(d -> d.tags().contains(tag.trim()));
        }
        return stream.sorted(comparator(sort)).toList();
    }

    public DishDtos.DishDetail detail(Long dishId) {
        Dish dish = approvedDish(dishId);
        return new DishDtos.DishDetail(dishViewService.card(dish), dishViewService.reviewViews(dishId));
    }

    public List<DishDtos.DishCard> recommendations(Integer limit) {
        int size = limit == null ? 10 : Math.max(1, Math.min(limit, 30));
        return dishMapper.selectList(Wrappers.<Dish>lambdaQuery().eq(Dish::getStatus, "APPROVED")).stream()
                .map(dishViewService::card)
                .sorted(Comparator.comparing(dishViewService::weightedScore).reversed())
                .limit(size)
                .toList();
    }

    public List<String> tags() {
        return dishTagMapper.selectList(Wrappers.<DishTag>lambdaQuery().orderByAsc(DishTag::getTag)).stream()
                .map(DishTag::getTag)
                .distinct()
                .toList();
    }

    @Transactional
    public DishSubmission submit(DishDtos.DishSubmissionRequest request) {
        if (canteenMapper.selectById(request.canteenId()) == null) {
            throw BusinessException.notFound("canteen not found");
        }
        CanteenWindow window = windowMapper.selectById(request.windowId());
        if (window == null || !window.getCanteenId().equals(request.canteenId())) {
            throw BusinessException.badRequest("window does not belong to canteen");
        }
        DishSubmission submission = new DishSubmission();
        submission.setSubmitterId(SecurityUtils.currentUserId());
        submission.setCanteenId(request.canteenId());
        submission.setWindowId(request.windowId());
        submission.setName(request.name().trim());
        submission.setImageUrl(request.imageUrl());
        submission.setPrice(request.price());
        submission.setDescription(request.description());
        submission.setSpiceLevel(request.spiceLevel() == null ? 0 : request.spiceLevel());
        submission.setTags(String.join(",", request.tags() == null ? List.of() : request.tags()));
        submission.setAuditStatus("PENDING");
        submissionMapper.insert(submission);
        return submission;
    }

    @Transactional
    public Dish approveSubmission(Long submissionId, String reason) {
        DishSubmission submission = submissionMapper.selectById(submissionId);
        if (submission == null) {
            throw BusinessException.notFound("submission not found");
        }
        if (!"PENDING".equals(submission.getAuditStatus())) {
            throw BusinessException.conflict("submission already audited");
        }
        Dish dish = new Dish();
        dish.setCanteenId(submission.getCanteenId());
        dish.setWindowId(submission.getWindowId());
        dish.setName(submission.getName());
        dish.setImageUrl(submission.getImageUrl());
        dish.setPrice(submission.getPrice() == null ? BigDecimal.ZERO : submission.getPrice());
        dish.setDescription(submission.getDescription());
        dish.setSpiceLevel(submission.getSpiceLevel());
        dish.setStatus("APPROVED");
        dishMapper.insert(dish);
        for (String tag : splitTags(submission.getTags())) {
            DishTag dishTag = new DishTag();
            dishTag.setDishId(dish.getId());
            dishTag.setTag(tag);
            dishTagMapper.insert(dishTag);
        }
        submission.setAuditStatus("APPROVED");
        submission.setAuditReason(reason);
        submission.setApprovedDishId(dish.getId());
        submissionMapper.updateById(submission);
        return dish;
    }

    public void rejectSubmission(Long submissionId, String reason) {
        DishSubmission submission = submissionMapper.selectById(submissionId);
        if (submission == null) {
            throw BusinessException.notFound("submission not found");
        }
        if (!"PENDING".equals(submission.getAuditStatus())) {
            throw BusinessException.conflict("submission already audited");
        }
        submission.setAuditStatus("REJECTED");
        submission.setAuditReason(reason);
        submissionMapper.updateById(submission);
    }

    public List<DishSubmission> pendingSubmissions() {
        return submissionMapper.selectList(Wrappers.<DishSubmission>lambdaQuery()
                .eq(DishSubmission::getAuditStatus, "PENDING")
                .orderByAsc(DishSubmission::getCreatedAt));
    }

    private Dish approvedDish(Long dishId) {
        Dish dish = dishMapper.selectById(dishId);
        if (dish == null || !"APPROVED".equals(dish.getStatus())) {
            throw BusinessException.notFound("dish not found");
        }
        return dish;
    }

    private List<String> splitTags(String tags) {
        if (tags == null || tags.isBlank()) {
            return List.of();
        }
        return Stream.of(tags.split(",")).map(String::trim).filter(s -> !s.isBlank()).distinct().toList();
    }

    private Comparator<DishDtos.DishCard> comparator(String sort) {
        return switch (sort == null ? "" : sort) {
            case "rating" -> Comparator.comparing(DishDtos.DishCard::avgRating).reversed();
            case "favorite" -> Comparator.comparing(DishDtos.DishCard::favoriteCount).reversed();
            case "price" -> Comparator.comparing(d -> d.price() == null ? BigDecimal.ZERO : d.price());
            default -> Comparator.comparing(DishDtos.DishCard::id);
        };
    }
}
