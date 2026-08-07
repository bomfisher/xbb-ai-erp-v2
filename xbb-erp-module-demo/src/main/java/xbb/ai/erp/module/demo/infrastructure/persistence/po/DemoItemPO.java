package xbb.ai.erp.module.demo.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("demo_item")
public class DemoItemPO extends BaseEntity {
    private String corpid;
    private Long dataId;
    private String name;
    private String creatorId;
    private String modifyId;
}
