package edu.thu.canteen.dto;

import edu.thu.canteen.domain.entity.CanteenWindow;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public final class CanteenDtos {
    private CanteenDtos() {
    }

    public record CanteenCard(
            Long id,
            String name,
            String coverUrl,
            String address,
            String openHours,
            String payMethods,
            Boolean onCampus,
            BigDecimal latitude,
            BigDecimal longitude,
            Double avgRating,
            Long dishCount,
            Double crowdLevel
    ) {
    }

    public record CrowdSummary(Double level, Long reportCount) {
    }

    public record CanteenDetail(
            CanteenCard base,
            List<CanteenWindow> windows,
            List<DishDtos.DishCard> dishes
    ) {
    }

    public record CrowdReportRequest(@NotNull @Min(1) @Max(5) Integer level) {
    }

    public record HeatPoint(Long canteenId, String canteenName, BigDecimal latitude, BigDecimal longitude, Long visits, BigDecimal amount) {
    }
}
