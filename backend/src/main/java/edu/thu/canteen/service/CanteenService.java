package edu.thu.canteen.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import edu.thu.canteen.common.BusinessException;
import edu.thu.canteen.domain.entity.*;
import edu.thu.canteen.domain.mapper.*;
import edu.thu.canteen.dto.CanteenDtos;
import edu.thu.canteen.dto.DishDtos;
import edu.thu.canteen.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class CanteenService {
    private final CanteenMapper canteenMapper;
    private final CanteenWindowMapper windowMapper;
    private final DishMapper dishMapper;
    private final ReviewMapper reviewMapper;
    private final CrowdReportMapper crowdReportMapper;
    private final ConsumptionRecordMapper consumptionRecordMapper;
    private final DishViewService dishViewService;

    public CanteenService(CanteenMapper canteenMapper, CanteenWindowMapper windowMapper, DishMapper dishMapper,
                          ReviewMapper reviewMapper, CrowdReportMapper crowdReportMapper,
                          ConsumptionRecordMapper consumptionRecordMapper, DishViewService dishViewService) {
        this.canteenMapper = canteenMapper;
        this.windowMapper = windowMapper;
        this.dishMapper = dishMapper;
        this.reviewMapper = reviewMapper;
        this.crowdReportMapper = crowdReportMapper;
        this.consumptionRecordMapper = consumptionRecordMapper;
        this.dishViewService = dishViewService;
    }

    public List<CanteenDtos.CanteenCard> list(String keyword, Boolean onCampus, String sort) {
        LambdaQueryWrapper<Canteen> query = Wrappers.<Canteen>lambdaQuery().eq(Canteen::getStatus, 1);
        if (keyword != null && !keyword.isBlank()) {
            query.like(Canteen::getName, keyword.trim());
        }
        if (onCampus != null) {
            query.eq(Canteen::getOnCampus, onCampus);
        }
        List<CanteenDtos.CanteenCard> cards = canteenMapper.selectList(query).stream().map(this::card).toList();
        Comparator<CanteenDtos.CanteenCard> comparator = switch (sort == null ? "" : sort) {
            case "rating" -> Comparator.comparing(CanteenDtos.CanteenCard::avgRating).reversed();
            case "crowd" -> Comparator.comparing(CanteenDtos.CanteenCard::crowdLevel);
            default -> Comparator.comparing(CanteenDtos.CanteenCard::id);
        };
        return cards.stream().sorted(comparator).toList();
    }

    public CanteenDtos.CanteenDetail detail(Long id, Integer floorNo, String tag, String sort) {
        Canteen canteen = activeCanteen(id);
        List<CanteenWindow> windows = windowMapper.selectList(Wrappers.<CanteenWindow>lambdaQuery()
                .eq(CanteenWindow::getCanteenId, id).eq(CanteenWindow::getStatus, 1)
                .orderByAsc(CanteenWindow::getFloorNo, CanteenWindow::getId));
        List<DishDtos.DishCard> dishes = dishMapper.selectList(Wrappers.<Dish>lambdaQuery()
                        .eq(Dish::getCanteenId, id).eq(Dish::getStatus, "APPROVED"))
                .stream()
                .map(dishViewService::card)
                .filter(d -> floorNo == null || floorNo.equals(d.floorNo()))
                .filter(d -> tag == null || tag.isBlank() || d.tags().contains(tag.trim()))
                .sorted(dishComparator(sort))
                .toList();
        return new CanteenDtos.CanteenDetail(card(canteen), windows, dishes);
    }

    public void reportCrowd(Long canteenId, Integer level) {
        activeCanteen(canteenId);
        CrowdReport report = new CrowdReport();
        report.setCanteenId(canteenId);
        report.setUserId(SecurityUtils.currentUserId());
        report.setLevel(level);
        crowdReportMapper.insert(report);
    }

    public List<CanteenDtos.HeatPoint> heatmap(String scope) {
        Long currentUserId = "mine".equals(scope) ? SecurityUtils.currentUserId() : null;
        return canteenMapper.selectList(Wrappers.<Canteen>lambdaQuery().eq(Canteen::getStatus, 1)).stream()
                .map(canteen -> {
                    LambdaQueryWrapper<ConsumptionRecord> query = Wrappers.<ConsumptionRecord>lambdaQuery()
                            .eq(ConsumptionRecord::getCanteenId, canteen.getId());
                    if (currentUserId != null) {
                        query.eq(ConsumptionRecord::getUserId, currentUserId);
                    }
                    List<ConsumptionRecord> records = consumptionRecordMapper.selectList(query);
                    BigDecimal amount = records.stream()
                            .map(ConsumptionRecord::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new CanteenDtos.HeatPoint(
                            canteen.getId(), canteen.getName(), canteen.getLatitude(), canteen.getLongitude(),
                            (long) records.size(), amount);
                })
                .toList();
    }

    private Canteen activeCanteen(Long id) {
        Canteen canteen = canteenMapper.selectById(id);
        if (canteen == null || canteen.getStatus() != 1) {
            throw BusinessException.notFound("canteen not found");
        }
        return canteen;
    }

    private CanteenDtos.CanteenCard card(Canteen canteen) {
        List<Dish> dishes = dishMapper.selectList(Wrappers.<Dish>lambdaQuery()
                .eq(Dish::getCanteenId, canteen.getId()).eq(Dish::getStatus, "APPROVED"));
        List<Long> dishIds = dishes.stream().map(Dish::getId).toList();
        List<Review> reviews = dishIds.isEmpty() ? List.of() : reviewMapper.selectList(Wrappers.<Review>lambdaQuery()
                .in(Review::getDishId, dishIds).eq(Review::getStatus, "APPROVED"));
        double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
        CanteenDtos.CrowdSummary crowd = crowdSummary(canteen.getId());
        return new CanteenDtos.CanteenCard(
                canteen.getId(), canteen.getName(), canteen.getCoverUrl(), canteen.getAddress(),
                canteen.getOpenHours(), canteen.getPayMethods(), canteen.getOnCampus(),
                canteen.getLatitude(), canteen.getLongitude(), round(avg), (long) dishes.size(), crowd.level());
    }

    private CanteenDtos.CrowdSummary crowdSummary(Long canteenId) {
        List<CrowdReport> reports = crowdReportMapper.selectList(Wrappers.<CrowdReport>lambdaQuery()
                .eq(CrowdReport::getCanteenId, canteenId)
                .ge(CrowdReport::getCreatedAt, LocalDateTime.now().minusMinutes(45)));
        double level = reports.stream().mapToInt(CrowdReport::getLevel).average().orElse(0.0);
        return new CanteenDtos.CrowdSummary(round(level), (long) reports.size());
    }

    private Comparator<DishDtos.DishCard> dishComparator(String sort) {
        return switch (sort == null ? "" : sort) {
            case "rating" -> Comparator.comparing(DishDtos.DishCard::avgRating).reversed();
            case "favorite" -> Comparator.comparing(DishDtos.DishCard::favoriteCount).reversed();
            case "price" -> Comparator.comparing(d -> d.price() == null ? BigDecimal.ZERO : d.price());
            default -> Comparator.comparing(DishDtos.DishCard::id);
        };
    }

    private Double round(double value) {
        return BigDecimal.valueOf(value).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }
}
