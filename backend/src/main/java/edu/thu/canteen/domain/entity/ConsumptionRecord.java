package edu.thu.canteen.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("consumption_records")
public class ConsumptionRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long canteenId;
    private BigDecimal amount;
    private LocalDateTime consumedAt;
}
