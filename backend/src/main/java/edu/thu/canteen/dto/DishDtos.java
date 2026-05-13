package edu.thu.canteen.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public final class DishDtos {
    private DishDtos() {
    }

    public record DishCard(
            Long id,
            Long canteenId,
            String canteenName,
            Long windowId,
            String windowName,
            Integer floorNo,
            String name,
            String imageUrl,
            BigDecimal price,
            String description,
            Integer spiceLevel,
            List<String> tags,
            Double avgRating,
            Long reviewCount,
            Long favoriteCount
    ) {
    }

    public record DishDetail(DishCard base, List<ReviewDtos.ReviewView> reviews) {
    }

    public record DishSubmissionRequest(
            @NotNull Long canteenId,
            @NotNull Long windowId,
            @NotBlank @Size(min = 2, max = 50) String name,
            @Size(max = 255) String imageUrl,
            BigDecimal price,
            @Size(max = 500) String description,
            @Min(0) @Max(5) Integer spiceLevel,
            List<@Size(max = 20) String> tags
    ) {
    }
}
