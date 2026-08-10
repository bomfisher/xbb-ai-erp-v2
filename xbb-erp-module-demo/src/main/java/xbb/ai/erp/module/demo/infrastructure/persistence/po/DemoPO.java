package xbb.ai.erp.module.demo.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("demo")
public class DemoPO extends BaseEntity {
    private String corpid;
    private String name;
    private String userId;
    private String depId;
    private String comb;
    private String combMulti;
    private Integer numInt;
    private java.math.BigDecimal numDouble;
    private java.math.BigDecimal amount;
    private Long date;
    private Long time;
    private String file;
    private String image;
    private String address;
    private String creatorId;
    private String modifyId;
}
