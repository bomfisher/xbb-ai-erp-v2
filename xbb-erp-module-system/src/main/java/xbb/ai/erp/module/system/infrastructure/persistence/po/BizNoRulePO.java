package xbb.ai.erp.module.system.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@TableName("sys_biz_no_rule")
public class BizNoRulePO extends BaseEntity {

    private String corpid;
    private String businessCode;
    private String prefix;
    private String ruleType;
}
