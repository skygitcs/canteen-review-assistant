package edu.thu.canteen.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("canteen_windows")
public class CanteenWindow {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long canteenId;
    private Integer floorNo;
    private String name;
    private String openHours;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
