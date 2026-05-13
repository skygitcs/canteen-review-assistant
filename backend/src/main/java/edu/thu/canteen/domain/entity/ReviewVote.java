package edu.thu.canteen.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("review_votes")
public class ReviewVote {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long reviewId;
    private Long userId;
    private Integer vote;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
