package xbb.ai.erp.module.system.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@TableName("sys_business_config")
public class BusinessConfigPO extends BaseEntity {

    private String corpid;
    private String businessCode;
    private String configJson;
}
