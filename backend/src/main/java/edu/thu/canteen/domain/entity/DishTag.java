package edu.thu.canteen.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("dish_tags")
public class DishTag {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long dishId;
    private String tag;
}
