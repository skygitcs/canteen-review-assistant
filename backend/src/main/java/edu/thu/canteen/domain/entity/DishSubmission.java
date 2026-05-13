package edu.thu.canteen.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("dish_submissions")
public class DishSubmission {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long submitterId;
    private Long canteenId;
    private Long windowId;
    private String name;
    private String imageUrl;
    private BigDecimal price;
    private String description;
    private Integer spiceLevel;
    private String tags;
    private String auditStatus;
    private String auditReason;
    private Long approvedDishId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
