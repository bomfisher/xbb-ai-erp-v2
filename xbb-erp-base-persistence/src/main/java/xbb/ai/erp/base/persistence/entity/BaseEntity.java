package xbb.ai.erp.base.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long addTime;
    private Long updateTime;
    private Integer del;

}
