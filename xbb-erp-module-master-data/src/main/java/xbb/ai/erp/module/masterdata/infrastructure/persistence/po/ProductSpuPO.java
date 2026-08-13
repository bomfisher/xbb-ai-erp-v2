package xbb.ai.erp.module.masterdata.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product_spu")
public class ProductSpuPO extends BaseEntity {
    private String corpid;
    private String spuCode;
    private String spuName;
    private String categoryName;
    private Integer enabled;
    private String remark;
    private String creatorId;
    private String modifyId;
}
