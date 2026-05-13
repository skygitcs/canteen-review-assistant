package edu.thu.canteen.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("canteens")
public class Canteen {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String coverUrl;
    private String address;
    private String openHours;
    private String payMethods;
    private Boolean onCampus;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
