package edu.thu.canteen.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("crowd_reports")
public class CrowdReport {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long canteenId;
    private Long userId;
    private Integer level;
    private LocalDateTime createdAt;
}
